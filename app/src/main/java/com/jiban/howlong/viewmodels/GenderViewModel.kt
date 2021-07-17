package com.jiban.howlong.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jiban.howlong.data.gender.Gender
import com.jiban.howlong.data.gender.GenderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject internal constructor(
    private val genderRepository: GenderRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getMyGender(myGender: String): Flow<Gender> = genderRepository.getMyGender(myGender)

}