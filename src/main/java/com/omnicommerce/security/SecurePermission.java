package com.omnicommerce.security;

import com.omnicommerce.user.authority.Authority;
import com.omnicommerce.user.authority.AuthorityReposity;
import com.omnicommerce.user.permission.Permission;
import com.omnicommerce.user.role.Role;
import com.omnicommerce.user.role.RoleRepository;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Log4j2
@Component("permission")
public class SecurePermission {
  public static final String READ = "READ";
  public static final String WRITE = "WRITE";

  @Autowired private AuthorityReposity authorityRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private RoleHierarchy roleHierarchy;

  public boolean ofRoles(String... roles) {
    Collection<GrantedAuthority> mustHaveRoles = new ArrayList<>();
    for (String role : roles) {
      mustHaveRoles.add(new SimpleGrantedAuthority(role));
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Set<Permission> securePermissions = new TreeSet<>(Permission.permissionComparator());
    Set<Permission> userPermissions = new TreeSet<>(Permission.permissionComparator());

    securePermissions.addAll(getPermissionSet(mustHaveRoles));
    userPermissions.addAll(getPermissionSet(authentication.getAuthorities()));

    return userPermissions.containsAll(securePermissions);
  }

  public Set<Permission> getPermissionSet(Collection<? extends GrantedAuthority> roles) {
    Collection<? extends GrantedAuthority> hierarchies =
        roleHierarchy.getReachableGrantedAuthorities(roles);

    Set<Authority> authorities = new HashSet<>();

    for (GrantedAuthority grantedAuthority : hierarchies) {
      Optional<Role> roleOptional = roleRepository.findByName(grantedAuthority.getAuthority());
      if (!roleOptional.isPresent()) return new HashSet<>();

      Role role = roleOptional.get();

      authorities.addAll(authorityRepository.findByAuthorityKeyRoleId(role.getId()));
    }

    return authorities.stream().map(Authority::getPermission).collect(Collectors.toSet());
  }

  public AuthorityReposity getAuthorityRepository() {
    return authorityRepository;
  }

  public void setAuthorityRepository(AuthorityReposity authorityRepository) {
    this.authorityRepository = authorityRepository;
  }

  public RoleHierarchy getRoleHierarchy() {
    return roleHierarchy;
  }

  public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
    this.roleHierarchy = roleHierarchy;
  }

  public RoleRepository getRoleRepository() {
    return roleRepository;
  }

  public void setRoleRepository(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }
}
