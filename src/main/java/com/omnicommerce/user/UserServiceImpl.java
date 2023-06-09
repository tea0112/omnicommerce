package com.omnicommerce.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(UserDTO userDTO) {
        User user = new User(null, userDTO.getEmail(), userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(user);
        return user;
    }
}
