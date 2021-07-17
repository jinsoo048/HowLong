package com.jiban.howlong.data.score

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT * FROM score_table WHERE range_Start = :myRange_Start")
    fun getMyScore(myRange_Start: String): Flow<Score>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(score: List<Score>)

    @Delete
    fun delete(score: Score)

}