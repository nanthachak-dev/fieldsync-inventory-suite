package com.example.fieldsync_inventory_backend.enums;

public enum SyncAction {
    CREATED,  // Record is new (createdAt > lastSyncTime, and deletedAt is null)
    UPDATED,  // Record was modified (updatedAt > lastSyncTime, and deletedAt is null)
    DELETED   // Record was soft-deleted (deletedAt > lastSyncTime)
}
