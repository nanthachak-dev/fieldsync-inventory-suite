package com.example.fieldsync_inventory_backend.repository.inventory;

import com.example.fieldsync_inventory_backend.entity.inventory.SeedBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeedBatchRepository extends JpaRepository<SeedBatchEntity, Long> {
    // naming method to match the rules of JPA
    // Check combined unique fields constraint
    boolean existsByVarietyIdAndYearAndSeasonIdAndGradingAndGenerationIdAndGermination(
            Integer riceVarietyId, Integer year, Integer seasonId, Boolean grading,
            Integer riceGenerationId, Boolean germination
    );

    Optional<SeedBatchEntity> findByVarietyIdAndYearAndSeasonIdAndGradingAndGenerationIdAndGermination(
            Integer riceVarietyId, Integer year, Integer seasonId, Boolean grading,
            Integer riceGenerationId, Boolean germination
    );
}
