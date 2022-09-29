package cmc.mellyserver.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalDataStarter implements CommandLineRunner {

    private final DataLoader dataLoader;

    @Override
    public void run(String... args) throws Exception {
        dataLoader.loadData();
    }
}