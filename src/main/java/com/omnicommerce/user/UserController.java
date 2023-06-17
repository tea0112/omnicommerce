package com.omnicommerce.user;

import com.omnicommerce.golobal.exception.LoginException;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import com.omnicommerce.user.position.Position;
import com.omnicommerce.user.position.PositionKey;
import com.omnicommerce.user.position.PositionRepository;
import com.omnicommerce.user.role.Role;
import com.omnicommerce.user.role.RoleRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
  @Autowired private RoleRepository roleRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private PositionRepository positionRepository;
  @Autowired private UserDetailsService userDetailsService;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<Object> signup(@RequestBody UserDTO userDTO)
      throws ConstraintViolationException {
    User user = userService.save(userDTO);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO)
      throws UserNotFoundException, LoginException {
    Map<String, String> BearerToken = new HashMap<>();
    BearerToken.put("BearerToken", userService.login(userDTO));
    return new ResponseEntity<>(BearerToken, HttpStatus.OK);
  }

  @GetMapping("/test-jwt")
  public String testJwt() {
    return "you are eligible to pass this wall";
  }

  @GetMapping("/seed")
  void seed() {
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
      newAdminPosition =
          new Position(new PositionKey(admin.getId(), newAdminRole.getId()), admin, newAdminRole);
      positionRepository.save(newAdminPosition);
    }
  }
}
