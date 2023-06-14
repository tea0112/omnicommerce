package com.omnicommerce.user.position;

import com.omnicommerce.user.User;
import com.omnicommerce.user.role.Role;

import javax.persistence.*;

@Entity
@Table(name = "users_roles")
public class Position {
    @EmbeddedId
    private PositionKey positionKey;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    public Position() {
    }

    public Position(PositionKey positionKey, User user, Role role) {
        this.positionKey = positionKey;
        this.user = user;
        this.role = role;
    }

    public PositionKey getPositionKey() {
        return positionKey;
    }

    public void setPositionKey(PositionKey positionKey) {
        this.positionKey = positionKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
