package com.jiban.howlong.data.male

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MaleDao {
    @Query("SELECT * FROM male_table WHERE score = :myScore")
    fun getMale(myScore: Int): Flow<Male>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(maleList: List<Male>)

    @Delete
    fun delete(male: Male)
}