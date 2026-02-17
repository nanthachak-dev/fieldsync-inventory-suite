package com.example.fieldsync_inventory_app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.fieldsync_inventory_app.data.local.database.RcrcSeedLocalDatabase
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestDatabaseModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context): RcrcSeedLocalDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            RcrcSeedLocalDatabase::class.java
        ).allowMainThreadQueries() // Allow queries on the main thread for simplicity in tests
            .build()
    }
}