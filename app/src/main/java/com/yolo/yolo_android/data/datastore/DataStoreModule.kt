package com.yolo.yolo_android.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.NotNull
import java.io.IOException

class DataStoreModule(val context: Context) {
    companion object {
        val KEY_USER_TOKEN = stringPreferencesKey("user_token")
        val KEY_LOGIN_TYPE = stringPreferencesKey("login_type")
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
        val KEY_NOTICE_NUM = stringSetPreferencesKey("notice_num")
        val KEY_PUSH_CONFIG = booleanPreferencesKey("push_config")
    }

    suspend fun <T> set(key: Preferences.Key<T>, @NotNull value: T) = when (value) {
        is Int -> context.dataStore.edit { it[key] = value }
        is Float -> context.dataStore.edit { it[key] = value }
        is Long -> context.dataStore.edit { it[key] = value }
        is Boolean -> context.dataStore.edit { it[key] = value }
        is String -> context.dataStore.edit { it[key] = value }
        is Set<*> -> context.dataStore.edit { it[key] = value }
        else -> null
    }

    suspend fun <T> get(key: Preferences.Key<T>): T? {
        return context.dataStore.data.map { it[key] }.firstOrNull()
    }

    suspend fun setLoginInfo(token: String, loginType: String, userId: String) {
        set(KEY_USER_TOKEN, token)
        set(KEY_LOGIN_TYPE, loginType)
        set(KEY_USER_ID, userId)
    }

    suspend fun clearLoginInfo() {
        context.dataStore.edit {
            it.remove(KEY_USER_TOKEN)
            it.remove(KEY_LOGIN_TYPE)
            it.remove(KEY_USER_ID)
            it.remove(KEY_FCM_TOKEN)
        }
    }

    suspend fun clearDataStore() {
        context.dataStore.edit {
            it.clear()
        }
    }

    val pushConfig: Flow<Boolean> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
                pref[KEY_PUSH_CONFIG] ?: true
            }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "yoloDataStore")