package com.omnicommerce.user.authority;

import com.omnicommerce.user.permission.Permission;
import com.omnicommerce.user.role.Role;
import javax.persistence.*;

@Entity
@Table(name = "roles_permissions")
public class Authority {
    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    Role role;
    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    Permission permission;
    @EmbeddedId
    private AuthorityKey authorityKey;

    public Authority() {
    }

    public Authority(AuthorityKey authorityKey, Role role, Permission permission) {
        this.authorityKey = authorityKey;
        this.role = role;
        this.permission = permission;
    }
}
