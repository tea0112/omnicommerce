package com.omnicommerce.user;

import com.omnicommerce.global.exception.UserException;

public interface UserService {
    User save(UserDTO userDTO) throws UserException;
    String login(UserDTO userDTO);
}
