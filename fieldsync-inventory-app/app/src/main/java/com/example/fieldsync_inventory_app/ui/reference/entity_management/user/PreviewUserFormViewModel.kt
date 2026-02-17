package com.example.fieldsync_inventory_app.ui.reference.entity_management.user

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.domain.model.AppUser
import com.example.fieldsync_inventory_app.domain.model.Role
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state.UserFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewUserFormViewModel : ViewModel(), IUserViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _userUiState = MutableStateFlow(UserUiState())
    override val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    private val _userFormUiState = MutableStateFlow(
        UserFormUiState(
            availableRoles = listOf(
                Role(id = 1, name = "ADMIN", description = "Administrator"),
                Role(id = 2, name = "USER", description = "Standard User"),
                Role(id = 3, name = "STAFF", description = "Staff Member")
            )
        )
    )
    override val userFormUiState: StateFlow<UserFormUiState> = _userFormUiState.asStateFlow()

    override val selectedUser: AppUser? = null
    override val isFirstLaunch: StateFlow<Boolean> = MutableStateFlow(false)

    override fun onEditUserItemClick(selectedUser: AppUser) {}
    override fun onAddNewUserClick() {}
    override fun onHideInactiveChange(hide: Boolean) {}
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
    override fun onSubmitClick() {}
    override fun firstLaunch() {}
    override fun onUserFormFirstLaunch() {}
}
