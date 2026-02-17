package com.example.fieldsync_inventory_backend.entity.stock;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_backend.entity.Auditable;
import com.example.fieldsync_inventory_backend.entity.user.AppUserEntity;
import org.hibernate.annotations.SQLDelete;

import java.time.Instant;

@Entity
@Table(name = "stock_transaction")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE stock_transaction SET deleted_at = NOW() WHERE id=?")
public class StockTransactionEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with StockTransactionType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_id", nullable = false)
    private StockTransactionTypeEntity transactionType;

    // Many-to-one relationship with AppUser
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    private AppUserEntity performedBy;

    @JoinColumn(name = "transaction_date", nullable = false)
    private Instant transactionDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}