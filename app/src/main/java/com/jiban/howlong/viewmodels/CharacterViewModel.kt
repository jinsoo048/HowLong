package com.jiban.howlong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jiban.howlong.data.character.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject internal constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    fun getMyCharacter(myCharacter: String) =
        characterRepository.getMyCharacter(myCharacter).asLiveData()

}