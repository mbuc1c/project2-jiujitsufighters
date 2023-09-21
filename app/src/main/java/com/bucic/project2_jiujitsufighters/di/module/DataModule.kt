package com.bucic.project2_jiujitsufighters.di.module

import com.bucic.project2_jiujitsufighters.db.dao.BJJFighterDao
import com.bucic.project2_jiujitsufighters.repository.BJJFighterRepository
import com.bucic.project2_jiujitsufighters.repository.BJJFighterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesBjjFighterRepositoryImpl(
        dao: BJJFighterDao
    ): BJJFighterRepository = BJJFighterRepositoryImpl(dao)

}