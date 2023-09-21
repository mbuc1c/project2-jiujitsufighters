package com.bucic.project2_jiujitsufighters.repository

import androidx.lifecycle.LiveData
import com.bucic.project2_jiujitsufighters.db.dao.BJJFighterDao
import com.bucic.project2_jiujitsufighters.model.BJJFighterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BJJFighterRepositoryImpl(
    private val dao: BJJFighterDao
) : BJJFighterRepository {

    override suspend fun fighters(): List<BJJFighterEntity>? = withContext(Dispatchers.IO) {
        dao.fighters()
    }

    override suspend fun getFighter(id: Int): BJJFighterEntity = withContext(Dispatchers.IO) {
        dao.getFighter(id)
    }

    override suspend fun insertFighter(fighter: BJJFighterEntity) = withContext(Dispatchers.IO) {
        dao.insertFighter(fighter)
    }

    override suspend fun deleteFighter(fighter: BJJFighterEntity) = withContext(Dispatchers.IO) {
        dao.deleteFighter(fighter)
    }

}