package com.jiban.howlong.data.sum

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SumRepository @Inject constructor(private val sumDao: SumDao) {
    fun getMySum(mySum: String) = sumDao.getMySum(mySum)
}