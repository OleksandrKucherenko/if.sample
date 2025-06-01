package com.sample.insurance.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.togglz.core.activation.Parameter;
import org.togglz.core.activation.ParameterBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.user.FeatureUser;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * A custom activation strategy that implements a limited A/B testing approach.
 * It activates the feature for a percentage of users until a maximum number of
 * activations is reached.
 * 
 * After that, it always returns false.
 */
public class LimitedABTestingStrategy implements ActivationStrategy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String ID = "limited-ab-testing";
    public static final String PARAM_PERCENTAGE = "percentage";
    public static final String PARAM_MAX_ACTIVATIONS = "maxActivations";
    public static final int PARAM_DEFAULT_PERCENTAGE = 50;
    public static final int PARAM_DEFAULT_MAX_ACTIVATIONS = 100;

    // Redis key prefix for storing activation counts
    private static final String REDIS_KEY_PREFIX = "togglz:ab-test-count:";

    @Autowired
    private JedisPool jedisPool;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Limited A/B Testing";
    }

    @Override
    public boolean isActive(FeatureState featureState, FeatureUser user) {
        if (jedisPool == null) {
            // Fallback to default behavior if Redis is not available
            return false;
        }

        String featureName = featureState.getFeature().name();
        String redisKey = REDIS_KEY_PREFIX + featureName;

        // Get parameters
        int percentage = getPercentage(featureState);
        int maxActivations = getMaxActivations(featureState);

        try (Jedis jedis = jedisPool.getResource()) {
            // Get current count
            String countStr = jedis.get(redisKey);
            long count = countStr != null ? Long.parseLong(countStr) : 0;

            // If we've exceeded the maximum activations, always return false
            if (count >= maxActivations) {
                return false;
            }

            // Increment the counter atomically
            var counter = jedis.incr(redisKey);
            logger.debug("Counter for {} is {}", featureName, counter);

            // Generate a random number between 0 and 100
            int random = (int) (Math.random() * 100);

            // Activate if the random number is less than the percentage
            return random < percentage;
        } catch (Exception e) {
            // Log the error and return false in case of Redis errors
            logger.error("Error accessing Redis for A/B testing.", e);
        }

        return false;
    }

    @Override
    public Parameter[] getParameters() {
        var percentageParam = ParameterBuilder.create(PARAM_PERCENTAGE)
                .label("Percentage")
                .matching("^(100|[1-9]?[0-9])$")
                .description("The percentage of invocations for which the feature should be active (0-100)");

        var maxActivationsParam = ParameterBuilder.create(PARAM_MAX_ACTIVATIONS)
                .label("Max Activations")
                .matching("\\d{1,5}")
                .description("The maximum number of activations for the feature (0-10000)");

        return new Parameter[] { percentageParam, maxActivationsParam };
    }

    private int getPercentage(FeatureState featureState) {
        var value = featureState.getParameter(PARAM_PERCENTAGE);

        // force 0..100 range via regex (just as a demo of alternative approach)
        if (value.matches("^(100|[1-9]?[0-9])$")) {
            return Math.min(Math.max(Integer.parseInt(value), 0), 100);
        } else {
            return PARAM_DEFAULT_PERCENTAGE; // Default to 50% if not specified or invalid
        }
    }

    private int getMaxActivations(FeatureState featureState) {
        var value = featureState.getParameter(PARAM_MAX_ACTIVATIONS);

        // force 0..10000 range via regex
        if (value.matches("\\d{1,5}")) {
            return Math.min(Math.max(Integer.parseInt(value), 0), 10000);
        } else {
            return PARAM_DEFAULT_MAX_ACTIVATIONS;
        }
    }
}
