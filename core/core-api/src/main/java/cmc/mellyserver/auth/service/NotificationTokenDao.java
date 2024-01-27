package cmc.mellyserver.auth.service;

import org.springframework.context.annotation.Configuration;

import cmc.mellyserver.dbredis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class NotificationTokenDao {

    private final RedisRepository repository;

    public void save(String key, String fcmToken) {
        repository.save(key, fcmToken);
    }

    public void remove(String key) {
        repository.delete(key);
    }
}
