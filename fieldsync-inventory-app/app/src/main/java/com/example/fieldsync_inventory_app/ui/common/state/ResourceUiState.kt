package com.example.fieldsync_inventory_app.ui.common.state

// Status of communication with network, database, and other resources
data class ResourceUiState (
    val data: Any? = null,
    val isLoading: Boolean = false,
    val loadingMessage: String? = null,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isInit: Boolean = true
)