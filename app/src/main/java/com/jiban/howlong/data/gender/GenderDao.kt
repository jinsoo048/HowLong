package com.jiban.howlong.data.gender

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GenderDao {
    @Query("SELECT * FROM gender_table WHERE gender = :myGender")
    fun getMyGender(myGender: String): Flow<Gender>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gender: List<Gender>)

    @Delete
    fun delete(gender: Gender)

}

