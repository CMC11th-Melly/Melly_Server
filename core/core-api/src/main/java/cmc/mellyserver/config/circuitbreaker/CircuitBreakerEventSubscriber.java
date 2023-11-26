package cmc.mellyserver.config.circuitbreaker;

import static cmc.mellyserver.config.circuitbreaker.CircuitBreakerConstants.*;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CircuitBreakerEventSubscriber implements MessageListener {

    final Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    public CircuitBreakerEventSubscriber(Resilience4JCircuitBreakerFactory circuitBreakerFactory,
        RedisMessageListenerContainer redisMessageListenerContainer) {
        ChannelTopic channelTopic = new ChannelTopic(CIRCUIT_OPEN);
        redisMessageListenerContainer.addMessageListener(this, channelTopic);
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        CircuitBreakerRegistry circuitBreakerRegistry = circuitBreakerFactory.getCircuitBreakerRegistry();
        String circuitBreakerName = new String(message.getBody()).replace("\"", "");
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        circuitBreaker.transitionToOpenState();
    }
}
