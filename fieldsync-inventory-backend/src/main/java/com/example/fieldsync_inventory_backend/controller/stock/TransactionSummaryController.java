package com.example.fieldsync_inventory_backend.controller.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.PagedResponse;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_summary.TransactionCountResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_summary.TotalSoldOutResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_summary.TransactionSummaryResponseDTO;
import com.example.fieldsync_inventory_backend.service.stock.TransactionSummaryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/stock-transaction-summaries")
@RequiredArgsConstructor
public class TransactionSummaryController {

    private final TransactionSummaryService service;

    @GetMapping
    public ResponseEntity<PagedResponse<TransactionSummaryResponseDTO>> getAllTransactionSummaries(
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @RequestParam(defaultValue = "false") boolean all,
            Pageable pageable) {

        Pageable effectivePageable = all ? Pageable.unpaged() : pageable;

        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(service.getTransactionSummariesByDateRange(startDate, endDate, effectivePageable));
        }

        return ResponseEntity.ok(service.getAllTransactionSummaries(effectivePageable));
    }

    @GetMapping("/count")
    public ResponseEntity<TransactionCountResponseDTO> getTransactionCount(
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate) {
        return ResponseEntity.ok(service.getTransactionCount(startDate, endDate));
    }

    @GetMapping("/total-sold-out")
    public ResponseEntity<TotalSoldOutResponseDTO> getTotalSoldOut(
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate) {
        return ResponseEntity.ok(service.getTotalSoldOut(startDate, endDate));
    }
}
