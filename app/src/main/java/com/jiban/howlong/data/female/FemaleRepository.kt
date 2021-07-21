package com.jiban.howlong.data.female

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FemaleRepository @Inject constructor(private val femaleDao: FemaleDao) {
    fun getFemale(myScore: Int) = femaleDao.getFemale(myScore)
}