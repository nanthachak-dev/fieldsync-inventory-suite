package com.example.fieldsync_inventory_backend.entity.inventory;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_backend.entity.Auditable;

import java.time.Instant;

@Entity
@Table(name = "seed_batch", uniqueConstraints = @UniqueConstraint(columnNames = {
        "variety_id", "year", "season_id", "grading", "generation_id", "germination"
}))
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeedBatchEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variety_id")
    private RiceVarietyEntity variety;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id")
    private SeasonEntity season;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "generation_id")
    private RiceGenerationEntity generation;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Boolean grading;

    @Column(nullable = false)
    private Boolean germination;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}