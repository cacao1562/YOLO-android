package com.yolo.yolo_android.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.NotNull


class DataStoreModule(val context: Context) {
    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    suspend fun <T> set(key: Preferences.Key<T>, @NotNull value: T) = when (value) {
        is Int -> context.dataStore.edit { it[key] = value }
        is Float -> context.dataStore.edit { it[key] = value }
        is Long -> context.dataStore.edit { it[key] = value }
        is Boolean -> context.dataStore.edit { it[key] = value }
        is String -> context.dataStore.edit { it[key] = value }
        else -> null
    }

    suspend fun <T> get(key: Preferences.Key<T>): T? {
        return context.dataStore.data.map { it[key] }.firstOrNull()
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "yoloDataStore")