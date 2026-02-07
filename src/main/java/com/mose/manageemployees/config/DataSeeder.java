package com.mose.manageemployees.config;

import com.mose.manageemployees.model.User;
import com.mose.manageemployees.model.UserRole;
import com.mose.manageemployees.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create default admin user if not exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@school.com");
                admin.setRole(UserRole.ADMIN);
                userRepository.save(admin);
                System.out.println("✅ Created default admin user (username: admin, password: admin123)");
            }

            // Create sample teacher
            if (userRepository.findByUsername("teacher1").isEmpty()) {
                User teacher = new User();
                teacher.setUsername("teacher1");
                teacher.setPassword(passwordEncoder.encode("teacher123"));
                teacher.setEmail("teacher@school.com");
                teacher.setRole(UserRole.TEACHER);
                userRepository.save(teacher);
                System.out.println("✅ Created sample teacher (username: teacher1, password: teacher123)");
            }

            // Create sample student
            if (userRepository.findByUsername("student1").isEmpty()) {
                User student = new User();
                student.setUsername("student1");
                student.setPassword(passwordEncoder.encode("student123"));
                student.setEmail("student@school.com");
                student.setRole(UserRole.STUDENT);
                userRepository.save(student);
                System.out.println("✅ Created sample student (username: student1, password: student123)");
            }
        };
    }
}
