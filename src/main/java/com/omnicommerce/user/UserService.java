package com.omnicommerce.user;

public interface UserService {
    User save(UserDTO userDTO);
    String login(UserDTO userDTO);
}
