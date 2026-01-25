package com.example.card_baseducilica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.card_baseducilica.data.AppDatabase
import com.example.card_baseducilica.data.entity.SetEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SetViewModel(application: Application) : AndroidViewModel(application) {

    private val setDao = AppDatabase
        .getInstance(application)
        .setDao()

    private val _sets = MutableStateFlow<List<SetEntity>>(emptyList())
    val sets: StateFlow<List<SetEntity>> = _sets

    /**
     * Dohvati setove za korisnika
     */
    fun loadSets(userId: Int) {
        viewModelScope.launch {
            _sets.value = setDao.getSetsForUser(userId)
        }
    }

    /**
     * Dodaj novi set
     */
    fun addSet(userId: Int, title: String, description: String?) {
        if (title.isBlank()) return

        viewModelScope.launch {
            setDao.insert(
                SetEntity(
                    userId = userId,
                    title = title,
                    description = description
                )
            )
            loadSets(userId)
        }
    }

    /**
     * Obriši set
     */
    fun deleteSet(setId: Int, userId: Int) {
        viewModelScope.launch {
            setDao.deleteById(setId)
            loadSets(userId)
        }
    }
}