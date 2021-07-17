package com.jiban.howlong.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jiban.howlong.data.character.Character
import com.jiban.howlong.data.character.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject internal constructor(
    private val characterRepository: CharacterRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getMyCharacter(myCharacter: String): Flow<Character> =
        characterRepository.getMyCharacter(myCharacter)

}