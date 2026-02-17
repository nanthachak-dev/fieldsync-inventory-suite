package com.example.fieldsync_inventory_backend.entity.procurement;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_backend.entity.Auditable;

import java.time.Instant;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_type_id")
    private SupplierTypeEntity supplierType;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(length = 50)
    private String email;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
