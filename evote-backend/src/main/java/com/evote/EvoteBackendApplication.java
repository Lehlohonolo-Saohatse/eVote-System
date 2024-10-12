package com.evote;

import com.evote.services.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EvoteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvoteBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAdminUser(AuthService authService) {
        return args -> {
            // Create an admin user on application startup
            authService.createAdminUser("admin@example.com", "adminpassword");
        };
    }
}