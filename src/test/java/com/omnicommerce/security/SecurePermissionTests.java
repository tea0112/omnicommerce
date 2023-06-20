package com.omnicommerce.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.omnicommerce.user.authority.Authority;
import com.omnicommerce.user.authority.AuthorityKey;
import com.omnicommerce.user.authority.AuthorityReposity;
import com.omnicommerce.user.permission.Permission;
import com.omnicommerce.user.role.Role;
import com.omnicommerce.user.role.RoleRepository;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class SecurePermissionTests {

  @MockBean private AuthorityReposity authorityReposity;
  @MockBean private RoleRepository roleRepository;
  private RoleHierarchy roleHierarchy;

  @BeforeEach
  void init() {
    roleHierarchy = (new SecureRole()).roleHierarchy();
  }

  @Test
  void testPermissionGet() {
    SecurePermission securePermission = new SecurePermission();
    securePermission.setRoleHierarchy(roleHierarchy);
    securePermission.setAuthorityRepository(authorityReposity);
    securePermission.setRoleRepository(roleRepository);

    Role adminRole = new Role(1L, SecureRole.ADMIN);
    Role userRole = new Role(2L, SecureRole.USER);

    Permission readPermission = new Permission(1L, SecurePermission.READ);
    Permission writePermission = new Permission(2L, SecurePermission.WRITE);

    when(roleRepository.findByName(SecureRole.ADMIN)).thenReturn(Optional.of(adminRole));
    when(roleRepository.findByName(SecureRole.USER)).thenReturn(Optional.of(userRole));

    when(authorityReposity.findByAuthorityKeyRoleId(adminRole.getId()))
        .thenReturn(
            new HashSet<>(
                Collections.singletonList(
                    new Authority(
                        new AuthorityKey(writePermission.getId(), adminRole.getId()),
                        adminRole,
                        writePermission))));
    when(authorityReposity.findByAuthorityKeyRoleId(userRole.getId()))
        .thenReturn(
            new HashSet<>(
                Collections.singletonList(
                    new Authority(
                        new AuthorityKey(readPermission.getId(), userRole.getId()),
                        userRole,
                        readPermission))));

    List<Permission> wantedPermissionList =
        Arrays.asList(
            new Permission(1L, SecurePermission.READ), new Permission(2L, SecurePermission.WRITE));
    List<GrantedAuthority> actualGrantedAuthorities =
        Arrays.asList(
            new SimpleGrantedAuthority(SecureRole.ADMIN),
            new SimpleGrantedAuthority(SecureRole.USER));

    Set<Permission> wanted = new TreeSet<>(Permission.permissionComparator());
    Set<Permission> actual = new TreeSet<>(Permission.permissionComparator());

    wanted.addAll(wantedPermissionList);
    actual.addAll(securePermission.getPermissionSet(actualGrantedAuthorities));

    assertTrue(wanted.containsAll(actual));
    assertTrue(actual.containsAll(wanted));
  }

  @Test
  @WithMockUser(username = "username", authorities = SecureRole.ADMIN)
  void testOfRolesSuccessfully() {
    SecurePermission securePermission = new SecurePermission();
    securePermission.setRoleHierarchy(roleHierarchy);
    securePermission.setAuthorityRepository(authorityReposity);
    securePermission.setRoleRepository(roleRepository);

    Role adminRole = new Role(1L, SecureRole.ADMIN);
    Role userRole = new Role(2L, SecureRole.USER);

    Permission readPermission = new Permission(1L, SecurePermission.READ);
    Permission writePermission = new Permission(2L, SecurePermission.WRITE);

    when(roleRepository.findByName(SecureRole.ADMIN)).thenReturn(Optional.of(adminRole));
    when(roleRepository.findByName(SecureRole.USER)).thenReturn(Optional.of(userRole));

    when(authorityReposity.findByAuthorityKeyRoleId(adminRole.getId()))
        .thenReturn(
            new HashSet<>(
                Collections.singletonList(
                    new Authority(
                        new AuthorityKey(writePermission.getId(), adminRole.getId()),
                        adminRole,
                        writePermission))));
    when(authorityReposity.findByAuthorityKeyRoleId(userRole.getId()))
        .thenReturn(
            new HashSet<>(
                Collections.singletonList(
                    new Authority(
                        new AuthorityKey(readPermission.getId(), userRole.getId()),
                        userRole,
                        readPermission))));
    assertTrue(securePermission.ofRoles(SecureRole.ADMIN));
  }

  @Test
  @WithMockUser(username = "username", authorities = SecureRole.USER)
  void testOfRolesUnsuccessfully() {
    SecurePermission securePermission = new SecurePermission();
    securePermission.setRoleHierarchy(roleHierarchy);
    securePermission.setAuthorityRepository(authorityReposity);
    securePermission.setRoleRepository(roleRepository);

    Role adminRole = new Role(1L, SecureRole.ADMIN);
    Role userRole = new Role(2L, SecureRole.USER);

    Permission readPermission = new Permission(1L, SecurePermission.READ);
    Permission writePermission = new Permission(2L, SecurePermission.WRITE);

    when(roleRepository.findByName(SecureRole.ADMIN)).thenReturn(Optional.of(adminRole));
    when(roleRepository.findByName(SecureRole.USER)).thenReturn(Optional.of(userRole));

    when(authorityReposity.findByAuthorityKeyRoleId(adminRole.getId()))
        .thenReturn(
            new HashSet<>(
                Collections.singletonList(
                    new Authority(
                        new AuthorityKey(writePermission.getId(), adminRole.getId()),
                        adminRole,
                        writePermission))));
    when(authorityReposity.findByAuthorityKeyRoleId(userRole.getId()))
        .thenReturn(
            new HashSet<>(
                Collections.singletonList(
                    new Authority(
                        new AuthorityKey(readPermission.getId(), userRole.getId()),
                        userRole,
                        readPermission))));
    assertFalse(securePermission.ofRoles(SecureRole.ADMIN));
  }
}
