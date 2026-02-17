package com.example.fieldsync_inventory_backend.entity.user;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_backend.entity.Auditable;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserRoleId.class)
public class UserRoleEntity extends Auditable {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AppUserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;
}
