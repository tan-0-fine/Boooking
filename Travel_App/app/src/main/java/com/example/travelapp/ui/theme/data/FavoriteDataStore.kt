package com.example.travelapp.ui.theme.data

import androidx.datastore.preferences.core.edit

import android.content.Context
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("favorite_store")

object FavoriteKeys {
    val FAVORITES = stringSetPreferencesKey("favorites")
}

class FavoriteDataStore(private val context: Context) {

    fun getFavorites(): Flow<Set<String>> {
        return context.dataStore.data.map { prefs ->
            prefs[FavoriteKeys.FAVORITES] ?: emptySet()
        }
    }

    suspend fun saveFavorite(id: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[FavoriteKeys.FAVORITES] ?: emptySet()
            prefs[FavoriteKeys.FAVORITES] = current + id
        }
    }

    suspend fun removeFavorite(id: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[FavoriteKeys.FAVORITES] ?: emptySet()
            prefs[FavoriteKeys.FAVORITES] = current - id
        }
    }
}
