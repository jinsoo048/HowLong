package com.jiban.howlong.data.sum

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SumDao {
    @Query("SELECT * FROM sum_table WHERE psum_range_Start = :myPsum_range_Start")
    fun getMySum(myPsum_range_Start: String): Flow<Sum>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sum: List<Sum>)

    @Delete
    fun delete(sum: Sum)
}