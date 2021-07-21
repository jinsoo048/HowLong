package com.jiban.howlong.data.character

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(private val characterDao: CharacterDao) {
    fun getMyCharacter(myCharacter: String) = characterDao.getMyCharacter(myCharacter)
}

