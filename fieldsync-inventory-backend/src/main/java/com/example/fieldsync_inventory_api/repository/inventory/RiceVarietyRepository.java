package com.example.fieldsync_inventory_api.repository.inventory;

import com.example.fieldsync_inventory_api.entity.inventory.RiceVarietyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiceVarietyRepository extends JpaRepository<RiceVarietyEntity, Integer> {

}
