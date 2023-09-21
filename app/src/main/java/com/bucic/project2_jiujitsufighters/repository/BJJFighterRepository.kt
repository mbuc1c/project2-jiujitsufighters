package com.bucic.project2_jiujitsufighters.repository

import androidx.lifecycle.LiveData
import com.bucic.project2_jiujitsufighters.model.BJJFighterEntity

interface BJJFighterRepository {
    suspend fun fighters(): List<BJJFighterEntity>?
    suspend fun getFighter(id: Int): BJJFighterEntity
    suspend fun insertFighter(fighter: BJJFighterEntity)
    suspend fun deleteFighter(fighter: BJJFighterEntity)
}