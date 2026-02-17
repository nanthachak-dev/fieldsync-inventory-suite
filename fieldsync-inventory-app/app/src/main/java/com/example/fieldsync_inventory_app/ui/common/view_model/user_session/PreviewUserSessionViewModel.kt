package com.example.fieldsync_inventory_app.ui.common.view_model.user_session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewUserSessionViewModel : IUserSessionViewModel {
    override val username: StateFlow<String> = MutableStateFlow("Preview User")
    override val userId: StateFlow<Int> = MutableStateFlow(1)
    override fun loadUserData() {}
    override fun logout(onSuccess: () -> Unit) { onSuccess() }
}