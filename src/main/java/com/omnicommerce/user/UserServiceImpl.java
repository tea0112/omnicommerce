package com.omnicommerce.user;

import com.omnicommerce.global.exception.UserException;
import com.omnicommerce.token.util.TokenUtil;
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
    public User save(UserDTO userDTO) throws UserException {
        User user = new User(null, userDTO.getEmail(), userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
        return user;
    }

    @Override
    public String login(UserDTO userDTO) {
        return TokenUtil.generateToken(userDTO.getEmail());
    }
}
