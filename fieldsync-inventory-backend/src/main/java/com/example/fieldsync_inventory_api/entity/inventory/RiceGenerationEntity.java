package com.example.fieldsync_inventory_api.entity.inventory;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_api.entity.Auditable;

import java.time.Instant;

@Entity
@Table(name = "rice_generation")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiceGenerationEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}