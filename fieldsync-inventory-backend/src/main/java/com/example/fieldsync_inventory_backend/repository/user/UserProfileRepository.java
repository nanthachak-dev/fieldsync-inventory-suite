package com.example.fieldsync_inventory_backend.repository.user;

//=============================================================
// Repository
// This interface handles data access operations for UserProfile.
// It extends JpaRepository for basic CRUD functionality.
//=============================================================

import com.example.fieldsync_inventory_backend.entity.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Integer> {

}