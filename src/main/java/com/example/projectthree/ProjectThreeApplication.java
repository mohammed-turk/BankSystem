package com.example.projectthree;

import com.example.projectthree.Model.User;
import com.example.projectthree.Repository.AuthRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjectThreeApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(ProjectThreeApplication.class, args);
        AuthRepository authRepository = context.getBean(AuthRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        if (authRepository.findUserByUsername("admin") == null){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setName("admin");
            admin.setEmail("admin@bank.com");
            admin.setRole("ADMIN");
            authRepository.save(admin);
        }
    }
}