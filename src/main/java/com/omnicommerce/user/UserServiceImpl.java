package com.omnicommerce.user;

import com.omnicommerce.golobal.exception.ErrorCodes;
import com.omnicommerce.golobal.exception.LoginException;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import com.omnicommerce.token.util.TokenUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(UserDTO userDTO) throws ConstraintViolationException {
        User user = new User(null, userDTO.getEmail(), userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(user);
        return user;
    }

    @Override
    public String login(UserDTO userDTO) throws UserNotFoundException, LoginException {
        Optional<User> userOptional = userRepository.findByUsername(userDTO.getUsername());
        if (!userOptional.isPresent())
            throw new UserNotFoundException(ErrorCodes.E00003.getMessage());

        User user = userOptional.get();
        if (passwordEncoder.encode(userDTO.getPassword()).equals(user.getPassword()))
            throw new LoginException(ErrorCodes.E00004.getMessage());

        return TokenUtil.generateToken(user.getUsername());
    }
}
