package com.example.fieldsync_inventory_backend.repository.inventory;


import com.example.fieldsync_inventory_backend.entity.inventory.RiceGenerationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiceGenerationRepository extends JpaRepository<RiceGenerationEntity, Integer> {

}
