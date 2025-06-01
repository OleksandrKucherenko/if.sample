package com.sample.insurance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.spi.FeatureProvider;
import org.togglz.core.user.UserProvider;
import org.togglz.redis.RedisStateRepository;
import org.togglz.spring.security.SpringSecurityUserProvider;
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
     * User provider for Togglz admin console that integrates with Spring Security.
     * This ensures that only authenticated users with the ADMIN role can access the console.
     *
     * @return Spring Security integrated user provider
     */
    @Bean
    public UserProvider getUserProvider() {
        return new SpringSecurityUserProvider("ROLE_ADMIN");
    }
}
