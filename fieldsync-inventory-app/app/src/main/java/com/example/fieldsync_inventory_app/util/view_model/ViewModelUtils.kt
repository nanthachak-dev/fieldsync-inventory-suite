package com.example.fieldsync_inventory_app.util.view_model

import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

// Extension of ResourceUiState to simplify loading state management
fun MutableStateFlow<ResourceUiState>.startLoading(message: String? = null) {
    update { it.copy(isLoading = true, error = null, isSuccess = false, loadingMessage = message) }
}

fun MutableStateFlow<ResourceUiState>.endLoading(error: String? = null) {
    if (error != null) {
        update {
            it.copy(
                isLoading = false,
                isSuccess = false,
                error = error,
                loadingMessage = null
            )
        }
    } else {
        update { it.copy(isLoading = false, isSuccess = true, error = null, loadingMessage = null) }
    }
}
