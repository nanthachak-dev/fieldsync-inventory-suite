package com.example.fieldsync_inventory_api.entity.sales;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_api.entity.Auditable;

import java.time.Instant;

@Entity
@Table(name = "customer")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Many-to-one relationship with CustomerType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_type_id")
    private CustomerTypeEntity customerType;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(length = 50)
    private String email;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}