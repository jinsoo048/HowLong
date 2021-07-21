package com.jiban.howlong.data.female

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FemaleDao {
    @Query("SELECT * FROM female_table WHERE score = :myScore")
    fun getFemale(myScore: Int): Flow<Female>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(femaleList: List<Female>)

    @Delete
    fun delete(female: Female)
}