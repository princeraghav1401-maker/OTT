package com.streamverse.backend.config;

import com.streamverse.backend.entity.Role;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.repository.RoleRepository;
import com.streamverse.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        User admin = userRepository.findByEmail("prince.raghav1401@gmail.com")
                .orElse(null);

        if (admin == null) {
            admin = User.builder()
                    .name("Prince Raghav")
                    .email("prince.raghav1401@gmail.com")
                    .phone("7078378216")
                    .password(passwordEncoder.encode("123456"))
                    .role(adminRole)
                    .active(true)
                    .deleted(false)
                    .emailVerified(true)
                    .emailOtp(null)
                    .otpExpiry(null)
                    .build();

            userRepository.save(admin);
            System.out.println("ADMIN CREATED: prince.raghav1401@gmail.com / 123456");
            return;
        }

        admin.setName("Prince Raghav");
        admin.setPhone("7078378216");
        admin.setRole(adminRole);
        admin.setActive(true);
        admin.setDeleted(false);
        admin.setEmailVerified(true);
        admin.setEmailOtp(null);
        admin.setOtpExpiry(null);

        userRepository.save(admin);

        System.out.println("ADMIN READY: prince.raghav1401@gmail.com");
    }
}