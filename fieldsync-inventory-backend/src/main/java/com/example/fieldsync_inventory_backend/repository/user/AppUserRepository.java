package com.example.fieldsync_inventory_backend.repository.user;

import com.example.fieldsync_inventory_backend.entity.user.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Integer> {
    // Find by username (case-insensitive)
    Optional<AppUserEntity> findByUsernameIgnoreCase(String username);
    Optional<AppUserEntity> findByUsername(String username);

    // Returns true if the username exists, but belongs to a different user ID
    boolean existsByUsernameAndIdNot(String username, Integer id);
}