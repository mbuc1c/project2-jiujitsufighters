package com.bucic.project2_jiujitsufighters.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bucic.project2_jiujitsufighters.model.BJJFighterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BJJFighterDao {
    @Query("SELECT * FROM bjj_fighters")
    fun fighters(): List<BJJFighterEntity>?

    @Query("SELECT * FROM bjj_fighters WHERE id = :id")
    fun getFighter(id: Int): BJJFighterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFighter(fighter: BJJFighterEntity)

    @Delete
    suspend fun deleteFighter(fighter: BJJFighterEntity)
}