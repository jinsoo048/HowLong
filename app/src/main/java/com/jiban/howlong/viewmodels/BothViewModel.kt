package com.jiban.howlong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jiban.howlong.data.both.BothRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BothViewModel @Inject internal constructor(
    private val bothRepository: BothRepository
) : ViewModel() {

    fun getBoth(myScore: Int) = bothRepository.getBoth(myScore).asLiveData()

}