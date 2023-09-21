package com.bucic.project2_jiujitsufighters.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "bjj_fighters")
data class BJJFighterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val surname: String = "",
    val dob: Date?,
    val profilePicture: String = "",
    val beltColor: String = ""
)
