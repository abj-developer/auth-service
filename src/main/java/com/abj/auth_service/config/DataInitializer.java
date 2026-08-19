package com.abj.auth_service.config;

import com.abj.auth_service.entity.AuthUser;
import com.abj.auth_service.repository.AuthUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (authUserRepository.findByUsername("ambuj").isEmpty()) {

                AuthUser user = new AuthUser(
                        "ambuj",
                        passwordEncoder.encode("password"),
                        "USER",
                        true
                );

                authUserRepository.save(user);
            }

            if (authUserRepository.findByUsername("admin").isEmpty()) {

                AuthUser admin = new AuthUser(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        "ADMIN",
                        true
                );

                authUserRepository.save(admin);
            }
        };
    }
}