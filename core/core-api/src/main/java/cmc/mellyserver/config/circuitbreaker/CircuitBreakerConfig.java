package cmc.mellyserver.config.circuitbreaker;

import static cmc.mellyserver.config.circuitbreaker.CircuitBreakerConstants.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CircuitBreakerConfig {

    @Bean
    public RegistryEventConsumer<CircuitBreaker> myRegistryEventConsumer(CircuitBreakerEventPublisher redisPublisher) {

        return new RegistryEventConsumer<CircuitBreaker>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                CircuitBreaker.EventPublisher eventPublisher = entryAddedEvent.getAddedEntry().getEventPublisher();

                eventPublisher.onCallNotPermitted(event -> log.error("onCallNotPermitted {}", event));

                eventPublisher.onError(event -> log.error("onError {}", event));

                eventPublisher.onStateTransition(event -> {
                    log.error("onStateTransition {}", event);
                    publishCircuitOpenTopic(event, redisPublisher);
                });
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
            }
        };
    }

    private void publishCircuitOpenTopic(CircuitBreakerOnStateTransitionEvent event,
        CircuitBreakerEventPublisher redisPublisher) {
        if (openStateSpreadEnabled(event)) {
            redisPublisher.publish(new ChannelTopic(CIRCUIT_OPEN), event.getCircuitBreakerName());
        }
    }

    /*
    현재 Circuit의 상태가 OPEN이 아닌 경우에만 OPEN으로 상태를 다시 변경
    해당 조건이 없으면 계속 OPEN 이벤트가 발생해서 무한 루프가 발생합니다.
     */
    private boolean openStateSpreadEnabled(CircuitBreakerOnStateTransitionEvent event) {
        return (!event.getStateTransition().getFromState().equals(OPEN_STATE))
            && event.getStateTransition().getToState().name().equals(OPEN_STATE);
    }
}
