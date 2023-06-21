package com.omnicommerce.user;

import com.omnicommerce.golobal.exception.LoginException;
import com.omnicommerce.golobal.exception.SaveFailureException;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
  @Autowired private UserRepository userRepository;
  @Autowired private UserDetailsService userDetailsService;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<Object> signup(@RequestBody UserDTO userDTO)
      throws ConstraintViolationException, SaveFailureException {
    User user = userService.save(userDTO);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDTO userLoginDTO)
      throws UserNotFoundException, LoginException {
    Map<String, String> BearerToken = new HashMap<>();
    BearerToken.put("BearerToken", userService.login(userLoginDTO));
    return new ResponseEntity<>(BearerToken, HttpStatus.OK);
  }

  @PreAuthorize("@permission.ofRoles('ROLE_USER')")
  @GetMapping("/test-user-role-firewall")
  public String testUserRoleFirewall() {
    return "pass user role";
  }

  @PreAuthorize("@permission.ofRoles('ROLE_ADMIN')")
  @GetMapping("/test-admin-role-firewall")
  public String testAdminRoleFirewall() {
    return "pass admin role";
  }

  @GetMapping("/seed")
  void seed() {
    userService.seed();
  }
}
