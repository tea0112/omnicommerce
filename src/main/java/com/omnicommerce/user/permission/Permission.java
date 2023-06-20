package com.omnicommerce.user.permission;


import java.util.Comparator;
import javax.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission implements Comparable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String name;

    public Permission() {
    }

    public Permission(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Comparator<Permission> permissionComparator() {
        return Comparator.comparingLong(Permission::getId).thenComparing(Permission::getId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return -1;
        return Comparator.comparingLong(Permission::getId)
            .thenComparing(Permission::getName)
            .compare(this, (Permission) o);
    }
}
