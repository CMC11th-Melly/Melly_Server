package cmc.mellyserver.config.circuitbreaker;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CircuitBreakerEventPublisher {

    private final RedisTemplate redisTemplate;

    public void publish(ChannelTopic topic, String message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}