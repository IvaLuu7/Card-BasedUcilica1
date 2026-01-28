package com.example.card_baseducilica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.card_baseducilica.data.AppDatabase
import com.example.card_baseducilica.data.entity.CardEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LearningViewModel(application: Application) : AndroidViewModel(application) {

    private val cardDao = AppDatabase.getInstance(application).cardDao()

    private val _cards = MutableStateFlow<List<CardEntity>>(emptyList())
    val cards: StateFlow<List<CardEntity>> = _cards

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _showAnswer = MutableStateFlow(false)
    val showAnswer: StateFlow<Boolean> = _showAnswer

    private val _knownCount = MutableStateFlow(0)
    val knownCount: StateFlow<Int> = _knownCount

    private val _unknownCount = MutableStateFlow(0)
    val unknownCount: StateFlow<Int> = _unknownCount

    private val _finished = MutableStateFlow(false)
    val finished: StateFlow<Boolean> = _finished

    fun startLearning(setId: Int, onlyFavorites: Boolean) {
        viewModelScope.launch {
            _cards.value =
                if (onlyFavorites) {
                    cardDao.getFavoriteCardsForSet(setId)
                } else {
                    cardDao.getCardsForSet(setId)
                }

            _currentIndex.value = 0
            _knownCount.value = 0
            _unknownCount.value = 0
            _showAnswer.value = false
            _finished.value = _cards.value.isEmpty()
        }
    }

    fun revealAnswer() {
        _showAnswer.value = true
    }

    fun markKnown() {
        _knownCount.value++
        moveNext()
    }

    fun markUnknown() {
        _unknownCount.value++
        moveNext()
    }

    private fun moveNext() {
        if (_currentIndex.value >= _cards.value.lastIndex) {
            _finished.value = true
        } else {
            _currentIndex.value++
            _showAnswer.value = false
        }
    }
}