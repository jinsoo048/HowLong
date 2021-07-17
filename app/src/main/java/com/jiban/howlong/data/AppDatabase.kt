package com.jiban.howlong.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jiban.howlong.data.character.Character
import com.jiban.howlong.data.character.CharacterDao
import com.jiban.howlong.data.gender.Gender
import com.jiban.howlong.data.gender.GenderDao
import com.jiban.howlong.data.number.Number
import com.jiban.howlong.data.number.NumberDao
import com.jiban.howlong.data.score.Score
import com.jiban.howlong.data.score.ScoreDao
import com.jiban.howlong.data.sum.Sum
import com.jiban.howlong.data.sum.SumDao
import com.jiban.howlong.utilities.DATABASE_NAME
import com.jiban.howlong.workers.SeedDatabaseWorker

@Database(
    entities = [Character::class, Gender::class, Number::class, Score::class, Sum::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun genderDao(): GenderDao
    abstract fun numberDao(): NumberDao
    abstract fun scoreDao(): ScoreDao
    abstract fun sumDao(): SumDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()

        }
    }
}
