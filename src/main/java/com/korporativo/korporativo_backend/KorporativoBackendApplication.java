package com.korporativo.korporativo_backend;

import com.korporativo.korporativo_backend.model.Role;
import com.korporativo.korporativo_backend.model.User;
import com.korporativo.korporativo_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class KorporativoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KorporativoBackendApplication.class, args);
    }

    @Bean
    @Profile("!test") // no se ejecuta en tests
    public CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("{noop}admin");
                admin.setRole(Role.ROLE_ADMIN);

                User user = new User();
                user.setUsername("user");
                user.setPassword("{noop}user");
                user.setRole(Role.ROLE_USER);

                userRepository.save(admin);
                userRepository.save(user);
            }
        };
    }
}
