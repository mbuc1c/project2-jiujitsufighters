package com.bucic.project2_jiujitsufighters.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bucic.project2_jiujitsufighters.db.dao.BJJFighterDao
import com.bucic.project2_jiujitsufighters.model.BJJFighterEntity
import com.bucic.project2_jiujitsufighters.util.Converters

@Database(
    entities = [BJJFighterEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BJJFighterDatabase : RoomDatabase() {
    abstract fun bjjFighterDao(): BJJFighterDao

    companion object {

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE bjj_fighters ADD COLUMN dob INTEGER NULL")

            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Define your migration logic here if needed
                // This is where you make changes to the schema
                // Example: database.execSQL("ALTER TABLE table_name ADD COLUMN new_column_name TEXT")
            }
        }
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Define your migration logic here
                // For example, you can alter tables, add columns, etc.
                // This is necessary if your schema has changed.
            }
        }
    }
}