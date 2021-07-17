package com.jiban.howlong.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jiban.howlong.data.number.Number
import com.jiban.howlong.data.number.NumberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NumberViewModel @Inject internal constructor(
    private val numberRepository: NumberRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getMyNumber(myNumber: String): Flow<Number> = numberRepository.getMyNumber(myNumber)

}