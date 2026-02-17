package com.example.fieldsync_inventory_app.ui.reference.entity_management.user

import com.example.fieldsync_inventory_app.domain.model.AppUser
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state.UserFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state.UserUiState
import kotlinx.coroutines.flow.StateFlow

interface IUserViewModel {
    // Properties
    val resourceUiState: StateFlow<ResourceUiState>
    val userUiState: StateFlow<UserUiState>
    val userFormUiState: StateFlow<UserFormUiState>
    val selectedUser: AppUser?
    val isFirstLaunch: StateFlow<Boolean>

    // User screen functions
    fun onEditUserItemClick(selectedUser: AppUser)
    fun onAddNewUserClick()
    fun onHideInactiveChange(hide: Boolean)

    // User form functions
    fun onUsernameChange(username: String)
    fun onPasswordChange(password: String)
    fun onIsActiveChange(isActive: Boolean)
    fun onRolesChange(roles: List<String>)
    fun onRoleToggle(roleName: String)
    fun onSubmitClick()

    // General functions
    fun firstLaunch()
    fun onUserFormFirstLaunch()
}
