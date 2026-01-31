package com.example.collegeschedule.data.store

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.favoritesDataStore by preferencesDataStore("favorites_groups")

class FavoritesStore(private val context: Context) {

    private val FAVORITES_KEY = stringSetPreferencesKey("favorite_groups")

    val favoritesFlow: Flow<Set<String>> =
        context.favoritesDataStore.data.map { prefs ->
            prefs[FAVORITES_KEY] ?: emptySet()
        }

    suspend fun addFavorite(group: String) {
        context.favoritesDataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] = current + group
        }
    }

    suspend fun removeFavorite(group: String) {
        context.favoritesDataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] = current - group
        }
    }
}
