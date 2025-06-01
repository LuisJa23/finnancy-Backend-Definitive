package finnancy.back.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FireBaseConfig {

    /**  
     * Se indica en application-*.properties como:
     *   firebase.service-account-file=classpath:firebase-service-account.json
     */
    @Value("${firebase.service-account-file}")
    private String serviceAccountPath;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        // Quitamos el prefijo "classpath:" para usar ClassPathResource
        String resourceLocation = serviceAccountPath.replaceFirst("^classpath:", "");
        Resource resource = new ClassPathResource(resourceLocation);

        try (InputStream serviceAccount = resource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }
}