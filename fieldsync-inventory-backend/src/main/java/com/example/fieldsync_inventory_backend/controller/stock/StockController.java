package com.example.fieldsync_inventory_backend.controller.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.PagedResponse;
import com.example.fieldsync_inventory_backend.dto.stock.summary.StockSummaryResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.summary.StockVarietySummaryResponseDTO;
import com.example.fieldsync_inventory_backend.service.stock.StockMovementDetailsService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockMovementDetailsService service;

    @GetMapping
    public ResponseEntity<StockSummaryResponseDTO> getStockSummary(
            @RequestParam(value = "lastDate", required = false) Instant lastDate) {
        return ResponseEntity.ok(service.getStockSummary(lastDate));
    }

    @GetMapping("/varieties")
    public ResponseEntity<PagedResponse<StockVarietySummaryResponseDTO>> getStockVarietySummary(
            @RequestParam(value = "lastDate", required = false) Instant lastDate,
            Pageable pageable) {
        return ResponseEntity.ok(service.getStockVarietySummary(lastDate, pageable));
    }
}
