package com.example.fieldsync_inventory_app.domain.repository.app_user

import com.example.fieldsync_inventory_app.domain.model.AppUser

interface AppUserRepository {
    suspend fun getAppUsers(): List<AppUser>
    suspend fun getAppUserById(id: Int): AppUser
    suspend fun saveAppUser(appUser: AppUser): AppUser
}
