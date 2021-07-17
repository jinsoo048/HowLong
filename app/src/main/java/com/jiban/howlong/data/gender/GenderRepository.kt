package com.jiban.howlong.data.gender

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenderRepository @Inject constructor(private val genderDao: GenderDao) {
    fun getMyGender(myGender: String) = genderDao.getMyGender(myGender)
}