package com.sample.insurance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.spi.FeatureProvider;
import org.togglz.core.user.SimpleFeatureUser;
import org.togglz.core.user.UserProvider;
import org.togglz.redis.RedisStateRepository;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Configuration for Togglz feature toggle integration.
 */
@Configuration
public class TogglzConfig {

    @Value("${redis.host:localhost}")
    private String redisHost;

    @Value("${redis.port:6379}")
    private int redisPort;

    @Bean
    @SuppressWarnings("unchecked")
    public FeatureProvider featureProvider() {
        return new EnumBasedFeatureProvider(Features.class);
    }

    /**
     * Redis state repository for Togglz feature toggles.
     *
     */
    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        
        return new JedisPool(poolConfig, redisHost, redisPort, 2000);
    }

    /**
     * Redis state repository for Togglz feature toggles.
     *
     * @param jedisPool JedisPool instance
     * @return Redis state repository
     */
    @Bean
    public StateRepository getRedisStateRepository(JedisPool jedisPool) {
        return new RedisStateRepository.Builder()
                .jedisPool(jedisPool)
                .keyPrefix("togglz:")
                .build();
    }

    /**
     * User provider for Togglz admin console.
     * For demonstration purposes, using a simple admin/admin credentials.
     *
     * @return User provider with admin rights
     */
    @Bean
    public UserProvider getUserProvider() {
        return () -> {
            SimpleFeatureUser user = new SimpleFeatureUser("admin");
            user.setAttribute("username", "admin");
            return user;
        };
    }
}
