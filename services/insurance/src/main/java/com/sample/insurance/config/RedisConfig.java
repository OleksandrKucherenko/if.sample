package com.sample.insurance.config;

import com.sample.insurance.model.Insurance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis configuration for the insurance service.
 */
@Configuration
public class RedisConfig {

    @Value("${redis.host:localhost}")
    private String redisHost;

    @Value("${redis.port:6379}")
    private int redisPort;
    
    /**
     * Creates a reactive Redis connection factory
     *
     * @return Reactive Redis connection factory
     */
    @Bean
    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        var configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        
        var clientConfig = LettuceClientConfiguration.builder().build();
                
        return new LettuceConnectionFactory(configuration, clientConfig);
    }

    /**
     * Configure ReactiveRedisTemplate for Insurance objects.
     *
     * @param connectionFactory Reactive Redis connection factory
     * @return ReactiveRedisTemplate for Insurance objects
     */
    @Bean
    public ReactiveRedisTemplate<String, Insurance> reactiveRedisInsuranceTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {

        var serializer = new Jackson2JsonRedisSerializer<>(Insurance.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Insurance> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        var context = builder
                .value(serializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
