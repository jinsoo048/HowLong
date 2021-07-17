package com.jiban.howlong.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jiban.howlong.data.score.Score
import com.jiban.howlong.data.score.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject internal constructor(
    private val scoreRepository: ScoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getMyScore(myScore: String): Flow<Score> = scoreRepository.getMyScore(myScore)

}