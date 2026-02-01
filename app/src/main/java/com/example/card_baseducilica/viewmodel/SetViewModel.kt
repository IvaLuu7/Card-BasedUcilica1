package com.example.card_baseducilica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.card_baseducilica.data.AppDatabase
import com.example.card_baseducilica.data.entity.SetEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.card_baseducilica.export.*

class SetViewModel(application: Application) : AndroidViewModel(application) {

    private val setDao = AppDatabase.getInstance(application).setDao()

    private val _sets = MutableStateFlow<List<SetEntity>>(emptyList())
    val sets: StateFlow<List<SetEntity>> = _sets

    private var currentUserId: Int? = null

    fun loadSets(userId: Int) {
        currentUserId = userId
        viewModelScope.launch {
            _sets.value = setDao.getSetsForUser(userId)
        }
    }

    fun addSet(userId: Int, title: String, description: String?) {
        if (title.isBlank()) return
        viewModelScope.launch {
            setDao.insert(SetEntity(userId = userId, title = title, description = description))
            loadSets(userId)
        }
    }

    fun deleteSet(setId: Int, userId: Int) {
        viewModelScope.launch {
            setDao.deleteById(setId)
            loadSets(userId)
        }
    }

    fun updateSet(setId: Int, title: String, description: String) {
        viewModelScope.launch {
            setDao.updateSet(setId, title, description)
            currentUserId?.let { loadSets(it) }   // 🔥 AUTOMATSKI REFRESH
        }
    }

    fun importSetFromJson(userId: Int, json: String, cardViewModel: CardViewModel) {
        viewModelScope.launch {
            try {
                val parsed = JsonImporter.fromJson(json)
                val newSetId = setDao.insertSetAndReturnId(
                    SetEntity(userId = userId, title = parsed.title, description = "Importirano")
                ).toInt()

                parsed.cards.forEach { card ->
                    cardViewModel.addCard(newSetId, card.question, card.answer)
                }

                loadSets(userId)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
