package com.example.fieldsync_inventory_backend.entity.user;

//=============================================================
// Entity
// This class represents the user_profile table in the database.
//=============================================================

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_backend.entity.Auditable;

import java.time.Instant;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileEntity extends Auditable {
    // Shared primary key with AppUser
    @Id
    @Column(name = "user_id")
    private Integer userId;

    // One-to-one relationship with AppUser
    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUserEntity appUser;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(length = 50)
    private String email;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}