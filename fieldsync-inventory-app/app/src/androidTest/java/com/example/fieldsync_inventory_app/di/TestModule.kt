package com.example.fieldsync_inventory_app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import com.example.fieldsync_inventory_app.data.local.dao.RiceVarietyDao
import com.example.fieldsync_inventory_app.data.local.database.RcrcSeedLocalDatabase
import com.example.fieldsync_inventory_app.domain.repository.rice_variety.RiceVarietyRepository
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestModule {

    // For testing, we provide a mock of the repository.
    // We are now mocking the interface directly, which does not have the "final" issue.
    @Provides
    @Singleton
    fun provideMockRepository(): RiceVarietyRepository {
        return Mockito.mock(RiceVarietyRepository::class.java)
    }

    // Since we are replacing AppModule, we also need to provide all its dependencies.
    // However, since we are only testing the ViewModel and the Repository,
    // we can provide mocks for other dependencies.
    // This provides a mock for the database, which is a dependency of the DAO.
    @Provides
    @Singleton
    fun provideMockDatabase(): RcrcSeedLocalDatabase {
        return Mockito.mock(RcrcSeedLocalDatabase::class.java)
    }

    // Provides a mock for the DAO, which is a dependency of the repository.
    @Provides
    fun provideMockRiceVarietyDao(): RiceVarietyDao {
        return Mockito.mock(RiceVarietyDao::class.java)
    }
}