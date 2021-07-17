package com.jiban.howlong.data.number

import javax.inject.Inject

class NumberRepository @Inject constructor(private val numberDao: NumberDao) {
    fun getMyNumber(myNumber: String) = numberDao.getMyNumber(myNumber)
}