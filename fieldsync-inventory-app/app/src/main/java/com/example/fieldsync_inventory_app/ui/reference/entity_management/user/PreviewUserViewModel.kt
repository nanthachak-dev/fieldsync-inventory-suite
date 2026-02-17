package com.example.fieldsync_inventory_app.ui.reference.entity_management.user

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.domain.model.AppUser
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state.UserFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewUserViewModel : ViewModel(), IUserViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _userUiState = MutableStateFlow(
        UserUiState(
            users = listOf(
                AppUser(id = 1, username = "admin", roles = listOf("ADMIN", "USER"), isActive = true),
                AppUser(id = 2, username = "staff1", roles = listOf("USER"), isActive = true),
                AppUser(id = 3, username = "staff2", roles = listOf("USER"), isActive = false)
            )
        )
    )
    override val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    private val _userFormUiState = MutableStateFlow(UserFormUiState())
    override val userFormUiState: StateFlow<UserFormUiState> = _userFormUiState.asStateFlow()

    private var _selectedUser: AppUser? = null
    override val selectedUser: AppUser?
        get() = _selectedUser

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun onEditUserItemClick(selectedUser: AppUser) {
        _selectedUser = selectedUser
    }

    override fun onAddNewUserClick() {
        _selectedUser = null
    }

    override fun onHideInactiveChange(hide: Boolean) {
        _userUiState.value = _userUiState.value.copy(hideInactive = hide)
    }

    override fun onUsernameChange(username: String) {
        _userFormUiState.value = _userFormUiState.value.copy(username = username)
    }

    override fun onPasswordChange(password: String) {
        _userFormUiState.value = _userFormUiState.value.copy(password = password)
    }

    override fun onIsActiveChange(isActive: Boolean) {
        _userFormUiState.value = _userFormUiState.value.copy(isActive = isActive)
    }

    override fun onRolesChange(roles: List<String>) {
        _userFormUiState.value = _userFormUiState.value.copy(roles = roles)
    }

    override fun onRoleToggle(roleName: String) {
        val currentRoles = _userFormUiState.value.roles.toMutableList()
        if (currentRoles.contains(roleName)) {
            currentRoles.remove(roleName)
        } else {
            currentRoles.add(roleName)
        }
        onRolesChange(currentRoles)
    }

    override fun onSubmitClick() {
        // No-op for preview
    }

    override fun firstLaunch() {
        _isFirstLaunch.value = false
    }

    override fun onUserFormFirstLaunch() {}
}
