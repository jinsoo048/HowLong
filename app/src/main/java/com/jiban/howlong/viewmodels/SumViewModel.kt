package com.jiban.howlong.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jiban.howlong.data.sum.Sum
import com.jiban.howlong.data.sum.SumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SumViewModel @Inject internal constructor(
    private val sumRepository: SumRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getMySum(mySum: String): Flow<Sum> = sumRepository.getMySum(mySum)

}