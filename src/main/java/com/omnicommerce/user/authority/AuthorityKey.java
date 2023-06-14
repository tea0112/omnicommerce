package com.omnicommerce.user.authority;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@Embeddable
public class AuthorityKey implements Serializable {
    @Column(name = "permission_id")
    private Long permissionId;
    @Column(name = "role_id")
    private Long roleId;

    public AuthorityKey() {
    }

    public AuthorityKey(Long permissionId, Long roleId) {
        this.permissionId = permissionId;
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
