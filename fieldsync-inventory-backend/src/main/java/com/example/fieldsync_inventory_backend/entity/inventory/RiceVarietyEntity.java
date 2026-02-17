package com.example.fieldsync_inventory_backend.entity.inventory;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import com.example.fieldsync_inventory_backend.entity.Auditable;

import java.time.Instant;

@Entity
@Table(name = "rice_variety")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiceVarietyEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name="image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
