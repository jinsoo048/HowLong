package com.jiban.howlong.data.number

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NumberDao {
    @Query("SELECT * FROM number_table WHERE number LIKE :myNumber")
    fun getMyNumber(myNumber: String): Flow<Number>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(number: List<Number>)

    @Delete
    fun delete(number: Number)
}


