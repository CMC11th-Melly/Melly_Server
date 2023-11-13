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

  @PostConstruct
  public void initializeFCM() throws IOException {

	try {
	  Resource resource = new ClassPathResource("firebase-key.json");
	  FileInputStream fis = new FileInputStream(resource.getFile());

	  FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(fis))
		  .build();

	  if (FirebaseApp.getApps().isEmpty()) {
		FirebaseApp.initializeApp(options);
	  }
	} catch (IOException e) {
	  log.error("Failed initializing FilebaseApp: {}", e.getMessage());
	}

  }

}