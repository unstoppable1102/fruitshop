package com.bkap.fruitshop.configuration;

import com.bkap.fruitshop.entity.Role;
import com.bkap.fruitshop.entity.User;
import com.bkap.fruitshop.repository.RoleRepository;
import com.bkap.fruitshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository){
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()){
                // Tìm role "ADMIN" hoặc tạo mới nếu chưa tồn tại
                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseGet(() -> roleRepository.save(new Role("ADMIN")));
                // Tạo danh sách roles
                Set<Role> roles = Set.of(adminRole);
                User adminUser = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .fullName("Phạm Thanh Minh")
                        .phone("0123456789")
                        .email("admin@gmail.com")
                        .address("HN")
                        .gender(true)
                        .status(true)
                        .roles(roles)
                        .build();
                try {
                    userRepository.save(adminUser);
                    log.warn("admin user has been created with default password: admin, please change password");
                } catch (Exception e) {
                    log.error("Failed to create admin user", e);
                }
            }
        };
    }
}
