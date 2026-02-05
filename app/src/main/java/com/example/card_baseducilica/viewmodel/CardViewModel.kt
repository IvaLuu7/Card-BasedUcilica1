package com.example.card_baseducilica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.card_baseducilica.data.AppDatabase
import com.example.card_baseducilica.data.entity.CardEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : AndroidViewModel(application) {

    private val cardDao = AppDatabase.getInstance(application).cardDao()

    private val _cards = MutableStateFlow<List<CardEntity>>(emptyList())
    val cards: StateFlow<List<CardEntity>> = _cards

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites

    fun loadCards(setId: Int) {
        viewModelScope.launch {
            _cards.value =
                if (_showOnlyFavorites.value) {
                    cardDao.getFavoriteCardsForSet(setId)
                } else {
                    cardDao.getCardsForSet(setId)
                }
        }
    }

    fun addCard(setId: Int, question: String, answer: String) {
        if (question.isBlank() || answer.isBlank()) return

        viewModelScope.launch {
            cardDao.insert(
                CardEntity(
                    setId = setId,
                    question = question,
                    answer = answer
                )
            )
            loadCards(setId)
        }
    }

    fun deleteCard(cardId: Int, setId: Int) {
        viewModelScope.launch {
            cardDao.deleteById(cardId)
            loadCards(setId)
        }
    }

    fun toggleFavorite(card: CardEntity) {
        viewModelScope.launch {
            cardDao.updateFavorite(
                cardId = card.id,
                isFavorite = !card.isFavorite
            )

            loadCards(card.setId)
        }
    }

    fun toggleFavoriteFilter(setId: Int) {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
        loadCards(setId)
    }

    fun updateCard(cardId: Int, question: String, answer: String) {
        viewModelScope.launch {
            cardDao.updateCard(cardId, question, answer)
        }
    }


}