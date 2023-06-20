package com.omnicommerce.user;

import com.omnicommerce.golobal.exception.ErrorCodes;
import com.omnicommerce.golobal.exception.LoginException;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import com.omnicommerce.security.SecureRole;
import com.omnicommerce.token.util.TokenUtil;
import com.omnicommerce.user.position.Position;
import com.omnicommerce.user.position.PositionKey;
import com.omnicommerce.user.position.PositionRepository;
import com.omnicommerce.user.role.Role;
import com.omnicommerce.user.role.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private UserDetailsService userDetailsService;
  @Autowired private RoleRepository roleRepository;
  @Autowired private PositionRepository positionRepository;

  @Override
  public User save(UserDTO userDTO) throws ConstraintViolationException {
    User user =
        new User(
            null,
            userDTO.getEmail(),
            userDTO.getUsername(),
            passwordEncoder.encode(userDTO.getPassword()));
    user = userRepository.save(user);
    return user;
  }

  @Override
  public String login(UserDTO userDTO) throws UserNotFoundException, LoginException {
    Optional<User> userOptional = userRepository.findByUsername(userDTO.getUsername());
    if (!userOptional.isPresent()) throw new UserNotFoundException(ErrorCodes.E00003.getMessage());

    User user = userOptional.get();
    if (passwordEncoder.encode(userDTO.getPassword()).equals(user.getPassword()))
      throw new LoginException(ErrorCodes.E00004.getMessage());

    return TokenUtil.generateToken(user.getUsername());
  }

  @Override
  public void seed() {
    Optional<UserDetails> adminOptional =
        Optional.ofNullable(userDetailsService.loadUserByUsername("admin"));
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

    Optional<Role> userRole = roleRepository.findByName(SecureRole.USER);
    Optional<Role> adminRole = roleRepository.findByName(SecureRole.ADMIN);
    Role newUserRole = null;
    if (!userRole.isPresent()) {
      newUserRole = new Role(null, SecureRole.USER);
      roleRepository.save(newUserRole);
    } else {
      newUserRole = userRole.get();
    }
    Role newAdminRole = null;
    if (!adminRole.isPresent()) {
      newAdminRole = new Role(null, SecureRole.ADMIN);
      roleRepository.save(newAdminRole);
    } else {
      newAdminRole = adminRole.get();
    }

    List<Position> positions = positionRepository.findByPositionKeyUserId(admin.getId());
    Position newAdminPosition = null;
    if (positions.size() == 0) {
      newAdminPosition =
          new Position(new PositionKey(admin.getId(), newAdminRole.getId()), admin, newAdminRole);
      positionRepository.save(newAdminPosition);
    }
  }
}
