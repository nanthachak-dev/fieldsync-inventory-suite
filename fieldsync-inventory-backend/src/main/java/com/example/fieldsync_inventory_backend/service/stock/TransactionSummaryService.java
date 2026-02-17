package com.example.fieldsync_inventory_backend.service.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.PagedResponse;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_summary.TotalSoldOutResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_summary.TransactionCountResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_summary.TransactionSummaryResponseDTO;
import com.example.fieldsync_inventory_backend.entity.view.TransactionSummaryEntity;
import com.example.fieldsync_inventory_backend.mapper.TransactionSummaryMapper;
import com.example.fieldsync_inventory_backend.repository.stock.TransactionSummaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TransactionSummaryService {

    private final TransactionSummaryRepository repository;
    private final TransactionSummaryMapper mapper;

    public PagedResponse<TransactionSummaryResponseDTO> getAllTransactionSummaries(Pageable pageable) {
        Page<TransactionSummaryEntity> page = repository.findAll(pageable);
        return PagedResponse.fromPage(page.map(mapper::toResponseDTO));
    }

    public PagedResponse<TransactionSummaryResponseDTO> getTransactionSummariesByDateRange(Instant startDate,
            Instant endDate, Pageable pageable) {
        Page<TransactionSummaryEntity> page = repository.findAllByTransactionDateBetween(startDate, endDate,
                pageable);
        return PagedResponse.fromPage(page.map(mapper::toResponseDTO));
    }

    public TransactionCountResponseDTO getTransactionCount(Instant startDate, Instant endDate) {
        if (endDate == null) {
            endDate = Instant.now();
        }
        if (startDate == null) {
            startDate = endDate.minus(30, ChronoUnit.DAYS);
        }

        long count = repository.countByTransactionDateBetween(startDate, endDate);

        return TransactionCountResponseDTO.builder()
                .totalTransactions(count)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public TotalSoldOutResponseDTO getTotalSoldOut(Instant startDate, Instant endDate) {
        if (endDate == null) {
            endDate = Instant.now();
        }
        if (startDate == null) {
            startDate = endDate.minus(30, java.time.temporal.ChronoUnit.DAYS);
        }

        BigDecimal totalSoldOut = repository.sumTotalSalePriceByDateRangeAndMovementType(startDate, endDate, "SALE");

        if (totalSoldOut == null) {
            totalSoldOut = BigDecimal.ZERO;
        }

        return TotalSoldOutResponseDTO.builder()
                .totalSoldOut(totalSoldOut)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
