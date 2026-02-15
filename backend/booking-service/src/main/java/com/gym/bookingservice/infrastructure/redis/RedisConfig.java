package com.gym.bookingservice.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gym.shared.events.BookingCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis Configuration for Publishing Events
 */
@Configuration
public class RedisConfig {

    public static final String BOOKING_CREATED_CHANNEL = "booking-created";

    @Bean
    public RedisTemplate<String, BookingCreatedEvent> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, BookingCreatedEvent> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serialize key as String
        template.setKeySerializer(new StringRedisSerializer());

        // ObjectMapper that supports Java 8 date/time (e.g. Instant)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<BookingCreatedEvent> serializer =
            new Jackson2JsonRedisSerializer<>(objectMapper, BookingCreatedEvent.class);
        template.setValueSerializer(serializer);

        return template;
    }
}
