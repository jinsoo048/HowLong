package com.jiban.howlong.data.both

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BothDao {
    @Query("SELECT * FROM both_table WHERE score = :myScore")
    fun getBoth(myScore: Int): Flow<Both>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(bothList: List<Both>)

    @Delete
    fun delete(both: Both)
}