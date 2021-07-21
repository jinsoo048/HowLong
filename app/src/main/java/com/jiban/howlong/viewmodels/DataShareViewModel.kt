package com.jiban.howlong.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jiban.howlong.data.StrengthSum

open class DataShareViewModel : ViewModel() {

    val data = MutableLiveData<StrengthSum>()

    fun data(strengthSum: StrengthSum) {
        data.value?.mySum = strengthSum.mySum
        data.value?.yourSum = strengthSum.mySum
        data.value?.totalSum = strengthSum.mySum
    }
}

