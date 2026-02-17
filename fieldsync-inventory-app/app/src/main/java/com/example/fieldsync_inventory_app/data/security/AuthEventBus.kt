package com.example.fieldsync_inventory_app.data.security

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun emitEvent(event: AuthEvent) {
        _events.tryEmit(event)
    }
}

sealed class AuthEvent {
    object Unauthorized : AuthEvent()
}
