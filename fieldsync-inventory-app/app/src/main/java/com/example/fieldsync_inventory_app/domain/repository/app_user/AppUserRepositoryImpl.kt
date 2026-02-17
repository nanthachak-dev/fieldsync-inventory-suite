package com.example.fieldsync_inventory_app.domain.repository.app_user

import android.util.Log
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.AppUserApi
import com.example.fieldsync_inventory_app.domain.model.AppUser
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUserRepositoryImpl @Inject constructor(
    private val api: AppUserApi
) : AppUserRepository {

    override suspend fun getAppUsers(): List<AppUser> {
        return api.getAppUsers().map { it.toDomain() }
    }

    override suspend fun getAppUserById(id: Int): AppUser {
        return api.getAppUserById(id).toDomain()
    }

    override suspend fun saveAppUser(appUser: AppUser): AppUser {
        try {
            return if (appUser.id == 0) {
                api.createAppUser(appUser.toRequestDto()).toDomain()
            } else {
                api.updateAppUser(appUser.id, appUser.toRequestDto()).toDomain()
            }
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AppUserRepositoryImpl", "saveAppUser >> Create user failed with error: $errorBody")
            throw Exception(errorBody)
        }catch (e: Exception){
            Log.e("AppUserRepositoryImpl", "saveAppUser >> Create user failed with unknown error: ${e.message}")
            throw e
        }
    }
}
