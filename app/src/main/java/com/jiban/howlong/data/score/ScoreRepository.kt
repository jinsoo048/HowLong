package com.jiban.howlong.data.score

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScoreRepository @Inject constructor(private val scoreDao: ScoreDao) {
    fun getMyScore(myScore: String) = scoreDao.getMyScore(myScore)
}