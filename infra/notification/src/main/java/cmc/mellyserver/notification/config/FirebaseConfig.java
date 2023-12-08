package cmc.mellyserver.notification.config;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FirebaseConfig {

    private static final String FIREBASE_KEY_FILE = "firebase-key.json";

    @PostConstruct
    public void initializeFCM() {
        try {
            Resource resource = new ClassPathResource(FIREBASE_KEY_FILE);
            FileInputStream stream = new FileInputStream(resource.getFile());

            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(stream))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            log.error("Failed initializing FilebaseApp: {}", e.getMessage());
        }

    }

}