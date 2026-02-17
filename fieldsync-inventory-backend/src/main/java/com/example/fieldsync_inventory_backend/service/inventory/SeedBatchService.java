package com.example.fieldsync_inventory_backend.service.inventory;

import com.example.fieldsync_inventory_backend.dto.inventory.seed_batch.SeedBatchRequestDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.seed_batch.SeedBatchResponseDTO;
import com.example.fieldsync_inventory_backend.entity.inventory.RiceGenerationEntity;
import com.example.fieldsync_inventory_backend.entity.inventory.RiceVarietyEntity;
import com.example.fieldsync_inventory_backend.entity.inventory.SeasonEntity;
import com.example.fieldsync_inventory_backend.entity.inventory.SeedBatchEntity;
import com.example.fieldsync_inventory_backend.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_backend.mapper.SeedBatchMapper;
import com.example.fieldsync_inventory_backend.repository.inventory.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SeedBatchService {

    private static final Logger logger = LoggerFactory.getLogger(SeedBatchService.class);

    private final SeedBatchRepository seedBatchRepo;
    private final RiceVarietyRepository varietyRepo;
    private final SeasonRepository seasonRepo;
    private final RiceGenerationRepository generationRepo;
    private final SeedConditionRepository conditionRepo;

    private final SeedBatchMapper seedBatchMapper; // Inject the mapper

    private SeedBatchEntity mapToEntity(SeedBatchRequestDTO dto) {

        // Make sure FKs exist
        RiceVarietyEntity variety = varietyRepo.findById(dto.getVarietyId())
                .orElseThrow(() -> new ResourceNotFoundException("RiceVariety record not found with id: "
                        + dto.getVarietyId()));
        SeasonEntity season = seasonRepo.findById(dto.getSeasonId())
                .orElseThrow(() -> new ResourceNotFoundException("Season record not found with id: "
                        + dto.getSeasonId()));
        RiceGenerationEntity generation = generationRepo.findById(dto.getGenerationId())
                .orElseThrow(() -> new ResourceNotFoundException("RiceGeneration record not found with id: "
                        + dto.getGenerationId()));

        return SeedBatchEntity.builder()
                .variety(variety)
                .year(dto.getYear())
                .season(season)
                .grading(dto.getGrading())
                .generation(generation)
                .germination(dto.getGermination())
                .description(dto.getDescription())
                .build();
    }

    @Transactional
    public SeedBatchResponseDTO createSeedBatch(SeedBatchRequestDTO dto) {
        logger.info("DTO of SeedBatch Request: {}", dto);
        // Check combined unique fields
        if (seedBatchRepo.existsByVarietyIdAndYearAndSeasonIdAndGradingAndGenerationIdAndGermination(
                dto.getVarietyId(), dto.getYear(), dto.getSeasonId(), dto.getGrading(),
                dto.getGenerationId(), dto.getGermination())) {
            throw new IllegalArgumentException("Duplicate seed batch entry");
        }

        SeedBatchEntity entity = mapToEntity(dto);

        // Save the entity and use MapStruct to map the response DTO
        SeedBatchEntity savedEntity = seedBatchRepo.save(entity);
        return seedBatchMapper.toResponseDTO(savedEntity);
    }

    public List<SeedBatchResponseDTO> getAllSeedBatches() {
        return seedBatchRepo.findAll().stream()
                .map(seedBatchMapper::toResponseDTO) // Use mapper
                .collect(Collectors.toList());
    }

    public SeedBatchResponseDTO getSeedBatchById(Long id) {
        SeedBatchEntity entity = seedBatchRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return seedBatchMapper.toResponseDTO(entity); // Use mapper
    }

    @Transactional
    public SeedBatchResponseDTO updateSeedBatch(Long id, SeedBatchRequestDTO dto) {
        logger.info("Update SeedBatch with data: {}", dto);

        SeedBatchEntity existing = seedBatchRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seed batch not found with id: " + id));

        // Get the references and update the existing entity's fields
        SeedBatchEntity updatedEntity = mapToEntity(dto);

        existing.setVariety(updatedEntity.getVariety());
        existing.setYear(updatedEntity.getYear());
        existing.setSeason(updatedEntity.getSeason());
        existing.setGrading(updatedEntity.getGrading());
        existing.setGeneration(updatedEntity.getGeneration());
        existing.setGermination(updatedEntity.getGermination());
        existing.setDescription(updatedEntity.getDescription());

        return seedBatchMapper.toResponseDTO(seedBatchRepo.save(existing)); // Use mapper
    }

    public void deleteSeedBatch(Long id) {
        seedBatchRepo.deleteById(id);
    }
}
