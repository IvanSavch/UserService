package com.innowise.userservice.config;

import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, User> redisUserTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        JacksonJsonRedisSerializer<User> userJacksonJsonRedisSerializer = new JacksonJsonRedisSerializer<>(objectMapper, User.class);
        redisTemplate.setValueSerializer(userJacksonJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;

    }
    @Bean
    public RedisTemplate<String, Card> redisCardTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, Card> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        JacksonJsonRedisSerializer<Card> userJacksonJsonRedisSerializer = new JacksonJsonRedisSerializer<>(objectMapper, Card.class);
        redisTemplate.setValueSerializer(userJacksonJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;

    }
}
