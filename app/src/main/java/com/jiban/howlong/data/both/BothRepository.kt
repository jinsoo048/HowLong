package com.jiban.howlong.data.both

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BothRepository @Inject constructor(private val bothDao: BothDao) {
    fun getBoth(myScore: Int) = bothDao.getBoth(myScore)
}