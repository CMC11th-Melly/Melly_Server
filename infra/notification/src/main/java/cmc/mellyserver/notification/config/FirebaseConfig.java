package cmc.mellyserver.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@Profile({ "local" })
public class FirebaseConfig {

	@PostConstruct
	public void initializeFCM() throws IOException {

		Resource resource = new ClassPathResource("firebase-key.json");
		FileInputStream fis = new FileInputStream(resource.getFile());

		FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(fis))
			.build();

		FirebaseApp.initializeApp(options);
	}

}