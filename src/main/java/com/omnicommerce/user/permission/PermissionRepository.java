package com.omnicommerce.user.permission;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
  Optional<Permission> findByName(String name);
}
