package com.example.fieldsync_inventory_app.ui.reference.entity_management.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.model.AppUser
import com.example.fieldsync_inventory_app.domain.use_case.app_user.GetAppUsersUseCase
import com.example.fieldsync_inventory_app.domain.use_case.app_user.SaveAppUserUseCase
import com.example.fieldsync_inventory_app.domain.use_case.role.GetRolesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state.UserFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state.UserUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getAppUsersUseCase: GetAppUsersUseCase,
    private val saveAppUserUseCase: SaveAppUserUseCase,
    private val getRolesUseCase: GetRolesUseCase
) : ViewModel(), IUserViewModel {

    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _userUiState = MutableStateFlow(UserUiState())
    override val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    private val _userFormUiState = MutableStateFlow(UserFormUiState())
    override val userFormUiState: StateFlow<UserFormUiState> = _userFormUiState.asStateFlow()

    override var selectedUser: AppUser? = null

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun onUserFormFirstLaunch(){
        loadAvailableRoles()
        _isFirstLaunch.update { false }
    }

    private fun loadAvailableRoles() {
        // If roles are already loaded, don't reload
        if (_userFormUiState.value.availableRoles.isNotEmpty()) return

        viewModelScope.launch {
            try {
                val roles = getRolesUseCase()
                _userFormUiState.update { it.copy(availableRoles = roles) }
            } catch (_: Exception) {
                // If it fails during init/setup, we'll try again when opening the form
                // Don't show error here as it might be too early (e.g. before login)
            }
        }
    }

    override fun firstLaunch() {
        loadUsers()
        _isFirstLaunch.update { false }
    }

    override fun onEditUserItemClick(selectedUser: AppUser) {
        _resourceUiState.update { it.copy(isSuccess = false, error = null) }
        this.selectedUser = selectedUser
        _userFormUiState.update {
            it.copy(
                username = selectedUser.username,
                password = "", // Don't pre-fill password for security/editing
                isActive = selectedUser.isActive,
                roles = selectedUser.roles,
                isEditing = true
            )
        }
        loadAvailableRoles()
    }

    override fun onAddNewUserClick() {
        _resourceUiState.update { it.copy(isSuccess = false, error = null) }
        this.selectedUser = null
        _userFormUiState.update {
            UserFormUiState(
                isEditing = false,
                availableRoles = _userFormUiState.value.availableRoles
            )
        }
        loadAvailableRoles()
    }

    override fun onHideInactiveChange(hide: Boolean) {
        _userUiState.update { it.copy(hideInactive = hide) }
    }

    override fun onUsernameChange(username: String) {
        _userFormUiState.update { it.copy(username = username) }
    }

    override fun onPasswordChange(password: String) {
        _userFormUiState.update { it.copy(password = password) }
    }

    override fun onIsActiveChange(isActive: Boolean) {
        _userFormUiState.update { it.copy(isActive = isActive) }
    }

    override fun onRolesChange(roles: List<String>) {
        _userFormUiState.update { it.copy(roles = roles) }
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
        if (_userFormUiState.value.isEditing) {
            updateUser()
        } else {
            createUser()
        }
    }

    private fun createUser() {
        if (_userFormUiState.value.username.isEmpty()) {
            _resourceUiState.update { it.copy(error = "Username is required") }
            return
        }
        if (_userFormUiState.value.password.isEmpty()) {
            _resourceUiState.update { it.copy(error = "Password is required") }
            return
        }

        val newUser = AppUser(
            username = _userFormUiState.value.username,
            password = _userFormUiState.value.password,
            roles = _userFormUiState.value.roles,
            isActive = _userFormUiState.value.isActive
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Creating user...")
                saveAppUserUseCase(newUser)
                loadUsers()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to create user: ${e.message}")
            }
        }
    }

    private fun updateUser() {
        if (_userFormUiState.value.username.isEmpty()) {
            _resourceUiState.update { it.copy(error = "Username is required") }
            return
        }

        val updatedUser = selectedUser?.copy(
            username = _userFormUiState.value.username,
            password = _userFormUiState.value.password.ifEmpty { null },
            roles = _userFormUiState.value.roles,
            isActive = _userFormUiState.value.isActive
        ) ?: return

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Updating user...")
                saveAppUserUseCase(updatedUser)
                loadUsers()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to update user: ${e.message}")
            }
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Loading users...")
                val users = getAppUsersUseCase()
                _userUiState.update { it.copy(users = users) }
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to load users: ${e.message}")
            }
        }
    }
}