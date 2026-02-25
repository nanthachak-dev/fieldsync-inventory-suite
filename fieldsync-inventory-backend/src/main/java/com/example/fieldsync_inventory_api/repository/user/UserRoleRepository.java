package com.example.fieldsync_inventory_api.repository.user;

import com.example.fieldsync_inventory_api.entity.user.UserRoleEntity;
import com.example.fieldsync_inventory_api.entity.user.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleId> {
    List<UserRoleEntity> findByUserId(Integer userId);
    List<UserRoleEntity> findByRoleId(Integer roleId);
}
