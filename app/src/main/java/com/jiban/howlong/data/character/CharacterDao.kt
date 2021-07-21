package com.jiban.howlong.data.character

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM character_table WHERE character LIKE :myCharacter")
    fun getMyCharacter(myCharacter: String): Flow<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characterList: List<Character>)

    @Delete
    fun delete(character: Character)
}