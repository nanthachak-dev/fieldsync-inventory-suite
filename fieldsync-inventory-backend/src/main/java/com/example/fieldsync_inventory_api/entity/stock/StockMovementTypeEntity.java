package com.example.fieldsync_inventory_api.entity.stock;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_api.entity.Auditable;
import com.example.fieldsync_inventory_api.enums.EffectOnStock;

import java.time.Instant;

@Entity
@Table(name = "stock_movement_type")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementTypeEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    /**
     * Defines the effect on stock. The `@Enumerated(EnumType.STRING)`
     * annotation tells JPA to store the enum's name (e.g., "IN" or "OUT")
     * as a string in the database column.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "effect_on_stock", nullable = false, length = 3)
    private EffectOnStock effectOnStock;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
