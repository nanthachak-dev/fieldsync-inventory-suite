package com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.domain.use_case.rice_variety.CreateOrUpdateRiceVarietyUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_variety.DeleteRiceVarietyUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_variety.GetLocalRiceVarietiesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_variety.SyncRiceVarietiesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.state.VarietyFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.state.VarietyUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RiceVarietyViewModel @Inject constructor(
    val getLocalRiceVarietiesUseCase: GetLocalRiceVarietiesUseCase,
    val syncRiceVarietiesUseCase: SyncRiceVarietiesUseCase,
    val createOrUpdateRiceVarietyUseCase: CreateOrUpdateRiceVarietyUseCase,
    val deleteVarietyUseCase: DeleteRiceVarietyUseCase
) : ViewModel(), IRiceVarietyViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _varietyUiState = MutableStateFlow(VarietyUiState())
    override val varietyUiState: StateFlow<VarietyUiState> = _varietyUiState.asStateFlow()

    private val _varietyFormUiState = MutableStateFlow(VarietyFormUiState())
    override val varietyFormUiState: StateFlow<VarietyFormUiState> =
        _varietyFormUiState.asStateFlow()

    override var selectedVariety: RiceVariety? = null

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()


    override fun firstLaunch() {
        // Load data to cards
        loadDataToCards()
        _isFirstLaunch.update { false }
    }

    override fun onEditVarietyItemClick(selectedVariety: RiceVariety) {
        _resourceUiState.update { it.copy(isSuccess = false) }

        this.selectedVariety = selectedVariety // VarietyFormScreen will use this get variety id
        _varietyFormUiState.update {
            it.copy(
                name = selectedVariety.name,
                description = selectedVariety.description,
                isEditing = true
            )
        }
    }

    override fun onDeleteVarietyItemClick(selectedVariety: RiceVariety) {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Deleting variety...")
                deleteVarietyUseCase.invoke(selectedVariety.id)
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to delete variety: ${e.message}")
            }
        }
    }

    // Set state of variety form for create operation
    override fun onAddNewVarietyClick() {
        _resourceUiState.update { it.copy(isSuccess = false) }
        _varietyFormUiState.update {
            it.copy(
                name = "",
                description = "",
                isEditing = false
            )
        }
    }

    override fun onNameChange(name: String) {
        _varietyFormUiState.update {
            it.copy(name = name)
        }
    }

    override fun onDescriptionChange(description: String) {
        _varietyFormUiState.update {
            it.copy(description = description)
        }
    }

    // Update or create variety
    override fun onSubmitClick() {
        // Check operation
        // Update mode
        if (_varietyFormUiState.value.isEditing) {
            updateVariety()
        }
        // Create mode
        if (!_varietyFormUiState.value.isEditing) {
            createVariety()
        }
    }

    // -- Helper Functions --
    // Create new variety on the server
    private fun createVariety() {
        // Validate inputs
        if (
            _varietyFormUiState.value.name.isEmpty()
        ) {
            _resourceUiState.update {
                it.copy(error = "Please fill in Name field")
            }
            return
        }

        // Set data to create new variety
        val newVariety = RiceVariety(
            id = 0,
            name = _varietyFormUiState.value.name,
            description = _varietyFormUiState.value.description,
            imageUrl = null,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            deletedAt = null
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Creating new variety...")
                createOrUpdateRiceVarietyUseCase.invoke(newVariety)
                selectedVariety = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception){
                _resourceUiState.endLoading("Failed to create new variety: ${e.message}")
            }
        }
    }

    // Update variety on the server
    private fun updateVariety() {
        // Validate inputs
        if (
            _varietyFormUiState.value.name.isEmpty()
        ) {
            _resourceUiState.update {
                it.copy(error = "Please fill in Name field")
            }
            return
        }

        val updatedVariety = selectedVariety?.copy(
            id = selectedVariety!!.id,
            name = _varietyFormUiState.value.name,
            description = _varietyFormUiState.value.description
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Updating variety...")
                createOrUpdateRiceVarietyUseCase.invoke(updatedVariety!!)
                selectedVariety = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception){
                _resourceUiState.endLoading("Failed to update variety: ${e.message}")
            }
        }
    }

    // -- Private Functions --
    private fun loadDataToCards() {
        // Sync data with server
        viewModelScope.launch {
            // Get rice varieties from server and save to local database
            try {
                _resourceUiState.startLoading("Retrieving rice varieties from the sever...")
                syncRiceVarietiesUseCase.sync()
                // Get rice varieties from local database
                getLocalRiceVarietiesUseCase().collect { varieties ->
                    _varietyUiState.update {
                        it.copy(varieties = varieties)
                    }

                    /**
                     * When calling getLocalRiceVarietiesUseCase().collect { ... },
                     * the coroutine pauses there and waits for new emissions from the database.
                     * Since database flows (like those from Room) are designed to stay open and listen for changes,
                     * the code execution never reaches the _resourceUiState.endLoading() line below it.
                     */

                    // Solution to prevent loading indicator doesn't disappear:
                    // Check if we are still in "loading" state.
                    // If yes, this is the FIRST emission, so hide the spinner.
                    if (_resourceUiState.value.isLoading) {
                        _resourceUiState.endLoading()
                    }
                }
                _resourceUiState.endLoading() // Success loading data
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to retrieve rice varieties: ${e.message}")
            }
        }
    }
}