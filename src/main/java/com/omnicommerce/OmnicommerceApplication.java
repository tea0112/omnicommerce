package com.omnicommerce;

import com.omnicommerce.user.User;
import com.omnicommerce.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class OmnicommerceApplication {
    @Autowired
    private UserRepository userRepository;
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
            Optional<UserDetails> user = Optional.ofNullable(userDetailsService.loadUserByUsername("admin"));
            user.ifPresent(System.out::print);
            if (!user.isPresent()) {
                User newUser = new User();
                newUser.setEmail("admin@gmail.com");
                newUser.setUsername("admin");
                newUser.setPassword(passwordEncoder.encode("admin"));
                userRepository.save(newUser);
            }
        };
    }
}
