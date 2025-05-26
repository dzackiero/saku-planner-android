package com.pnj.saku_planner.kakeibo.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserStorage @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile
    private var cachedToken: String? = null

    init {
        scope.launch {
            token.collect {
                cachedToken = it
            }
        }
    }

    suspend fun saveUser(token: String, name: String, email: String) {
        dataStore.edit {
            it[UserPreferencesKeys.TOKEN] = token
            it[UserPreferencesKeys.NAME] = name
            it[UserPreferencesKeys.EMAIL] = email
        }
        cachedToken = token
    }

    val token: Flow<String?> = dataStore.data.map { it[UserPreferencesKeys.TOKEN] }
    val name: Flow<String?> = dataStore.data.map { it[UserPreferencesKeys.NAME] }
    val email: Flow<String?> = dataStore.data.map { it[UserPreferencesKeys.EMAIL] }

    suspend fun clear() {
        dataStore.edit { it.clear() }
        cachedToken = null
    }

    fun getCachedToken(): String? = cachedToken
}


object UserPreferencesKeys {
    val TOKEN = stringPreferencesKey("token")
    val NAME = stringPreferencesKey("name")
    val EMAIL = stringPreferencesKey("email")
}