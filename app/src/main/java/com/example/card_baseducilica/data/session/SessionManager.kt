package com.example.card_baseducilica.data.session

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    private val KEY_USER_ID = longPreferencesKey("logged_in_user_id")

    val loggedInUserId: Flow<Long?> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_USER_ID]
        }

    suspend fun setLoggedInUser(userId: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = userId
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_USER_ID)
        }
    }
}