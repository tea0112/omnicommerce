package com.omnicommerce.user.authority;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityReposity extends JpaRepository<Authority, Long> {
  Set<Authority> findByAuthorityKeyRoleId(Long id);
}
