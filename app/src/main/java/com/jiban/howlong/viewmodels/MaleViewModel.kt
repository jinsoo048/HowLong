package com.jiban.howlong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jiban.howlong.data.male.MaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MaleViewModel @Inject internal constructor(
    private val maleRepository: MaleRepository
) : ViewModel() {

    fun getMale(myScore: Int) = maleRepository.getMale(myScore).asLiveData()

}