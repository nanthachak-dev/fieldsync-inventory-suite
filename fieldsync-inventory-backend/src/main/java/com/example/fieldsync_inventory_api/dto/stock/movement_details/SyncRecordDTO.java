package com.example.fieldsync_inventory_api.dto.stock.movement_details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.enums.SyncAction;

// This will be the main object in the list returned by the /sync endpoint
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncRecordDTO {
    private SyncAction action;
    private StockMovementDetailsResponseDTO record;
}
