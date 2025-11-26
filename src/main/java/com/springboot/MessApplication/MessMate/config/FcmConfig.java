package com.springboot.MessApplication.MessMate.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

//@Configuration
//public class FcmConfig {
//
//    @Bean
//    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
//        return FirebaseMessaging.getInstance(firebaseApp);
//    }
//
//    @Bean
//    FirebaseApp firebaseApp(GoogleCredentials credentials) {
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(credentials)
//                .build();
//
//        return FirebaseApp.initializeApp(options);
//    }
//
//    @Bean
//    GoogleCredentials googleCredentials() throws IOException {
//        return GoogleCredentials.getApplicationDefault();
//    }
//}
