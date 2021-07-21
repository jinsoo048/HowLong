package com.jiban.howlong.data.male

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaleRepository @Inject constructor(private val maleDao: MaleDao) {
    fun getMale(myScore: Int) = maleDao.getMale(myScore)
}
