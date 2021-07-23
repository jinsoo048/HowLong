package com.jiban.howlong.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jiban.howlong.data.StrengthBirthSum

open class DataBirthShareViewModel : ViewModel() {

    val data = MutableLiveData<StrengthBirthSum>()

    fun data(strengthBirthSum: StrengthBirthSum) {
        data.value?.myBirthSum = strengthBirthSum.myBirthSum
        data.value?.yourBirthSum = strengthBirthSum.myBirthSum
        data.value?.totalBirthSum = strengthBirthSum.myBirthSum
    }
}
