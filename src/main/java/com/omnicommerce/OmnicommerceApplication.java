package com.omnicommerce;

import com.omnicommerce.user.User;
import com.omnicommerce.user.UserRepository;
import com.omnicommerce.user.position.Position;
import com.omnicommerce.user.position.PositionKey;
import com.omnicommerce.user.position.PositionRepository;
import com.omnicommerce.user.role.Role;
import com.omnicommerce.user.role.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Log4j2
@SpringBootApplication
public class OmnicommerceApplication {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(OmnicommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            Optional<UserDetails> adminOptional = Optional.ofNullable(userDetailsService.loadUserByUsername("admin"));
            User admin = null;
            if (!adminOptional.isPresent()) {
                User newUser = new User();
                newUser.setEmail("admin@gmail.com");
                newUser.setUsername("admin");
                newUser.setPassword(passwordEncoder.encode("admin"));
                userRepository.save(newUser);
            } else {
                admin = (User) adminOptional.get();
            }

            Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
            Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
            Role newUserRole = null;
            if (!userRole.isPresent()) {
                newUserRole = new Role(null, "ROLE_USER");
                roleRepository.save(newUserRole);
            } else {
                newUserRole = userRole.get();
            }
            Role newAdminRole = null;
            if (!adminRole.isPresent()) {
                newAdminRole = new Role(null, "ROLE_ADMIN");
                roleRepository.save(newAdminRole);
            } else {
                newAdminRole = adminRole.get();
            }

            List<Position> positions = positionRepository.findByPositionKeyUserId(admin.getId());
            Position newAdminPosition = null;
            if (positions.size() == 0) {
                newAdminPosition = new Position(new PositionKey(admin.getId(), newAdminRole.getId()), admin, newAdminRole);
                positionRepository.save(newAdminPosition);
            }
        };
    }
}
