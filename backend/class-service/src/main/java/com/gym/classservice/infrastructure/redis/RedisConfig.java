package com.gym.classservice.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gym.classservice.application.events.BookingEventListener;
import com.gym.shared.events.BookingCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * Redis Configuration for Subscribing to Events
 */
@Configuration
public class RedisConfig {

    public static final String BOOKING_CREATED_CHANNEL = "booking-created";

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(BOOKING_CREATED_CHANNEL));

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(BookingEventListener eventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(eventListener, "handleBookingCreated");

        // ObjectMapper that supports Java 8 date/time (e.g. Instant) for deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<BookingCreatedEvent> serializer =
            new Jackson2JsonRedisSerializer<>(objectMapper, BookingCreatedEvent.class);
        adapter.setSerializer(serializer);

        return adapter;
    }
}
