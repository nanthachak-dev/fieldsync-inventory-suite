package com.example.fieldsync_inventory_api.entity.user;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite Key Class: UserRoleId
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleId implements Serializable {
    private Integer user;
    private Integer role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleId that)) return false;
        return Objects.equals(user, that.user) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }
}
