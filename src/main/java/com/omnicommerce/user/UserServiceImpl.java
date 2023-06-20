package com.omnicommerce.user;

import com.omnicommerce.golobal.exception.ErrorCodes;
import com.omnicommerce.golobal.exception.LoginException;
import com.omnicommerce.golobal.exception.SaveFailureException;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import com.omnicommerce.security.SecurePermission;
import com.omnicommerce.security.SecureRole;
import com.omnicommerce.token.util.TokenUtil;
import com.omnicommerce.user.authority.Authority;
import com.omnicommerce.user.authority.AuthorityKey;
import com.omnicommerce.user.authority.AuthorityReposity;
import com.omnicommerce.user.permission.Permission;
import com.omnicommerce.user.permission.PermissionRepository;
import com.omnicommerce.user.position.Position;
import com.omnicommerce.user.position.PositionKey;
import com.omnicommerce.user.position.PositionRepository;
import com.omnicommerce.user.role.Role;
import com.omnicommerce.user.role.RoleRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
  @Autowired private PermissionRepository permissionRepository;
  @Autowired private AuthorityReposity authorityRepository;

  @Transactional
  @Override
  public User save(UserDTO userDTO) throws ConstraintViolationException, SaveFailureException {
    User user =
        new User(
            null,
            userDTO.getEmail(),
            userDTO.getUsername(),
            passwordEncoder.encode(userDTO.getPassword()));
    user = userRepository.save(user);
    Optional<Role> userRoleOptional = roleRepository.findByName(SecureRole.USER);
    Role userRole = userRoleOptional.orElseThrow(SaveFailureException::new);

    Position userPosition =
        new Position(new PositionKey(user.getId(), userRole.getId()), user, userRole);
    userPosition = positionRepository.save(userPosition);
    user.setRoles(
        Collections.singletonList(new SimpleGrantedAuthority(userPosition.getRole().getName())));
    return user;
  }

  @Override
  public String login(UserDTO userDTO) throws UserNotFoundException, LoginException {
    Optional<User> userOptional = userRepository.findByUsername(userDTO.getUsername());

    User user =
        userOptional.orElseThrow(() -> new UserNotFoundException(ErrorCodes.E00003.getMessage()));

    if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword()))
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

    Optional<Role> userRoleOptional = roleRepository.findByName(SecureRole.USER);
    Optional<Role> adminRoleOptional = roleRepository.findByName(SecureRole.ADMIN);

    Role userRole =
        userRoleOptional.orElseGet(() -> roleRepository.save(new Role(null, SecureRole.USER)));
    Role adminRole =
        adminRoleOptional.orElseGet(() -> roleRepository.save(new Role(null, SecureRole.ADMIN)));

    List<Position> positions = positionRepository.findByPositionKeyUserId(admin.getId());
    Position newAdminPosition = null;
    if (positions.size() == 0) {
      newAdminPosition =
          new Position(new PositionKey(admin.getId(), adminRole.getId()), admin, adminRole);
      positionRepository.save(newAdminPosition);
    }

    Optional<Permission> readPermissionOptional =
        permissionRepository.findByName(SecurePermission.READ);
    Permission readPermission =
        readPermissionOptional.orElseGet(
            () -> permissionRepository.save(new Permission(null, SecurePermission.READ)));

    Optional<Permission> writePermissionOptional =
        permissionRepository.findByName(SecurePermission.WRITE);
    Permission writePermission =
        writePermissionOptional.orElseGet(
            () -> permissionRepository.save(new Permission(null, SecurePermission.WRITE)));

    Set<Authority> adminAuthorities =
        authorityRepository.findByAuthorityKeyRoleId(adminRole.getId());
    Authority adminWriteAuthority = null;
    if (adminAuthorities.size() == 0) {
      adminWriteAuthority =
          authorityRepository.save(
              new Authority(
                  new AuthorityKey(writePermission.getId(), adminRole.getId()),
                  adminRole,
                  writePermission));
    }

    Set<Authority> userAuthorities = authorityRepository.findByAuthorityKeyRoleId(userRole.getId());
    Authority userReadAuthority = null;
    if (userAuthorities.size() == 0) {
      userReadAuthority =
          authorityRepository.save(
              new Authority(
                  new AuthorityKey(readPermission.getId(), userRole.getId()),
                  userRole,
                  readPermission));
    }
  }
}
