package cmc.mellyserver.config.circuitbreaker;

import static cmc.mellyserver.config.circuitbreaker.CircuitBreakerConstants.*;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CircuitBreakerEventSubscriber implements MessageListener {

    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    private final RedisTemplate<String, Object> redisTemplate;

    public CircuitBreakerEventSubscriber(Resilience4JCircuitBreakerFactory circuitBreakerFactory,
        RedisMessageListenerContainer redisMessageListenerContainer, RedisTemplate redisTemplate) {
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.redisTemplate = redisTemplate;

        ChannelTopic channelTopic = new ChannelTopic(CIRCUIT_OPEN);
        redisMessageListenerContainer.addMessageListener(this, channelTopic);
    }

    /*
    서비스에서 관리되고 있는 서킷 브레이커 인스턴스를 조회해서 상태를 OPEN으로 변경합니다.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        CircuitBreakerRegistry circuitBreakerRegistry = circuitBreakerFactory.getCircuitBreakerRegistry();
        String circuitBreakerName = (String)redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.info("Subscribe, message is {}", circuitBreakerName);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        circuitBreaker.transitionToOpenState();
    }
}
