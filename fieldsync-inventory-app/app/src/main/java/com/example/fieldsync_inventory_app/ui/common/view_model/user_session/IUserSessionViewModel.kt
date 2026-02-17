package com.example.fieldsync_inventory_app.ui.common.view_model.user_session

import kotlinx.coroutines.flow.StateFlow

interface IUserSessionViewModel {
    val username: StateFlow<String>
    val userId: StateFlow<Int>
    fun loadUserData()
    fun logout(onSuccess: () -> Unit)
}