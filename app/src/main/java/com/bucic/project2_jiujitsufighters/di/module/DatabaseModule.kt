package com.bucic.project2_jiujitsufighters.di.module

import android.content.Context
import androidx.room.Room
import com.bucic.project2_jiujitsufighters.db.BJJFighterDatabase
import com.bucic.project2_jiujitsufighters.db.dao.BJJFighterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context): BJJFighterDatabase {
        return Room.databaseBuilder(
            applicationContext,
            BJJFighterDatabase::class.java,
            "bjj_fighters.db"
        ).addMigrations(
            BJJFighterDatabase.MIGRATION_1_2,
            BJJFighterDatabase.MIGRATION_2_3,
            BJJFighterDatabase.MIGRATION_3_4)
            .build()
    }

    @Provides
    fun providesGameDao(gameDatabase: BJJFighterDatabase): BJJFighterDao {
        return gameDatabase.bjjFighterDao()
    }
}