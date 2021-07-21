package com.jiban.howlong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jiban.howlong.data.female.FemaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FemaleViewModel @Inject internal constructor(
    private val femaleRepository: FemaleRepository
) : ViewModel() {

    fun getFemale(myScore: Int) = femaleRepository.getFemale(myScore).asLiveData()

}