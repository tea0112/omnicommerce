package com.omnicommerce.user;

import com.omnicommerce.golobal.exception.LoginException;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

public interface UserService {
    User save(UserDTO userDTO) throws ConstraintViolationException;
    String login(UserDTO userDTO) throws UserNotFoundException, LoginException;
}
