package com.example.fieldsync_inventory_app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.fieldsync_inventory_app.BuildConfig
import com.example.fieldsync_inventory_app.data.remote.api.AppUserApi
import com.example.fieldsync_inventory_app.data.remote.api.AuthApi
import com.example.fieldsync_inventory_app.data.remote.api.CustomerApi
import com.example.fieldsync_inventory_app.data.remote.api.CustomerTypeApi
import com.example.fieldsync_inventory_app.data.remote.api.InventoryApi
import com.example.fieldsync_inventory_app.data.remote.api.RiceGenerationApi
import com.example.fieldsync_inventory_app.data.remote.api.RiceVarietyApi
import com.example.fieldsync_inventory_app.data.remote.api.RoleApi
import com.example.fieldsync_inventory_app.data.remote.api.SeasonApi
import com.example.fieldsync_inventory_app.data.remote.api.SeedBatchApi
import com.example.fieldsync_inventory_app.data.remote.api.SeedConditionApi
import com.example.fieldsync_inventory_app.data.remote.api.StockMovementDetailsApi
import com.example.fieldsync_inventory_app.data.remote.api.StockMovementTypeApi
import com.example.fieldsync_inventory_app.data.remote.api.StockTransactionOperationApi
import com.example.fieldsync_inventory_app.data.remote.api.StockTransactionSummaryApi
import com.example.fieldsync_inventory_app.data.remote.api.StockTransactionTypeApi
import com.example.fieldsync_inventory_app.data.remote.api.StockApi
import com.example.fieldsync_inventory_app.data.remote.api.SupplierApi
import com.example.fieldsync_inventory_app.data.remote.api.SupplierTypeApi
import com.example.fieldsync_inventory_app.data.security.AuthEvent
import com.example.fieldsync_inventory_app.data.security.AuthEventBus
import com.example.fieldsync_inventory_app.data.security.AuthInterceptor
import com.example.fieldsync_inventory_app.domain.repository.data_store.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = BuildConfig.BASE_URL

    // Provide the OkHttpClient, injecting the AuthInterceptor 🔑
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenRepository: TokenRepository,
        authEventBus: AuthEventBus
    ): OkHttpClient {
        return OkHttpClient.Builder()
            // Add the custom interceptor first
            .addInterceptor(authInterceptor)
            // Add interceptor to handle 401 responses (Unauthorized)
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                
                // If it's a 401 and NOT the login endpoint, then it's a session expiration
                if (response.code == 401 && !request.url.toString().contains("/api/v1/auth/authenticate")) {
                    runBlocking {
                        tokenRepository.clearToken()
                    }
                    authEventBus.emitEvent(AuthEvent.Unauthorized)
                }
                response
            }
            // Add logging interceptor (optional, but useful)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Use the client configured with the interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRiceVarietyApi(retrofit: Retrofit): RiceVarietyApi {
        return retrofit.create(RiceVarietyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSeasonApi(retrofit: Retrofit): SeasonApi {
        return retrofit.create(SeasonApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRiceGenerationApi(retrofit: Retrofit): RiceGenerationApi {
        return retrofit.create(RiceGenerationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSeedConditionApi(retrofit: Retrofit): SeedConditionApi {
        return retrofit.create(SeedConditionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStockMovementTypeApi(retrofit: Retrofit): StockMovementTypeApi {
        return retrofit.create(StockMovementTypeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStockTransactionTypeApi(retrofit: Retrofit): StockTransactionTypeApi {
        return retrofit.create(StockTransactionTypeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCustomerTypeApi(retrofit: Retrofit): CustomerTypeApi {
        return retrofit.create(CustomerTypeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCustomerApi(retrofit: Retrofit): CustomerApi {
        return retrofit.create(CustomerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSeedBatchApi(retrofit: Retrofit): SeedBatchApi {
        return retrofit.create(SeedBatchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionOperationApi(retrofit: Retrofit): StockTransactionOperationApi {
        return retrofit.create(StockTransactionOperationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStockMovementDetailsApi(retrofit: Retrofit): StockMovementDetailsApi {
        return retrofit.create(StockMovementDetailsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSupplierTypeApi(retrofit: Retrofit): SupplierTypeApi {
        return retrofit.create(SupplierTypeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSupplierApi(retrofit: Retrofit): SupplierApi {
        return retrofit.create(SupplierApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppUserApi(retrofit: Retrofit): AppUserApi {
        return retrofit.create(AppUserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoleApi(retrofit: Retrofit): RoleApi {
        return retrofit.create(RoleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStockTransactionSummaryApi(retrofit: Retrofit): StockTransactionSummaryApi {
        return retrofit.create(StockTransactionSummaryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInventoryApi(retrofit: Retrofit): InventoryApi {
        return retrofit.create(InventoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStockApi(retrofit: Retrofit): StockApi {
        return retrofit.create(StockApi::class.java)
    }
}

