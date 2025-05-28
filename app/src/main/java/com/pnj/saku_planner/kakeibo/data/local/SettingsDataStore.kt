package com.pnj.saku_planner.kakeibo.data.local // Assuming this is the correct package

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey // For hour and minute
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first // To get initial value for cachedToken if needed
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val appDataStore = context.dataStore
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile
    private var cachedToken: String? = null

    init {
        scope.launch {
            cachedToken = appDataStore.data.map { it[UserPreferencesKeys.TOKEN] }.first()
        }
    }

    // --- User Session ---
    suspend fun saveUserSession(token: String, name: String, email: String) {
        appDataStore.edit { settings ->
            settings[UserPreferencesKeys.TOKEN] = token
            settings[UserPreferencesKeys.NAME] = name
            settings[UserPreferencesKeys.EMAIL] = email
        }
        cachedToken = token
    }

    suspend fun updateUserProfile(name: String, email: String) {
        appDataStore.edit { settings ->
            settings[UserPreferencesKeys.NAME] = name
            settings[UserPreferencesKeys.EMAIL] = email
        }
    }

    val token: Flow<String?> = appDataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.TOKEN]
    }
    val name: Flow<String?> = appDataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.NAME]
    }
    val email: Flow<String?> = appDataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.EMAIL]
    }

    val session: Flow<UserSession> = appDataStore.data.map { preferences ->
        UserSession(
            name = preferences[UserPreferencesKeys.NAME],
            email = preferences[UserPreferencesKeys.EMAIL]
        )
    }

    val settings: Flow<UserSettings> = appDataStore.data.map { preferences ->
        UserSettings(
            isDailySync = preferences[UserPreferencesKeys.DAILY_SYNC] ?: false,
            monthReflectionDate = preferences[UserPreferencesKeys.MONTH_REFLECTION_DATE] ?: 1,
            isMonthlyReflectNotification =
                preferences[UserPreferencesKeys.MONTH_REFLECTION_NOTIFICATION] ?: false,
        )
    }

    suspend fun setDailySync(isEnable: Boolean) {
        appDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.DAILY_SYNC] = isEnable
        }
    }

    suspend fun setMonthReflectionDate(date: Int) {
        appDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.MONTH_REFLECTION_DATE] = date
        }
    }

    suspend fun setMonthReflectionNotification(isEnable: Boolean) {
        appDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.MONTH_REFLECTION_NOTIFICATION] = isEnable
        }
    }


    val isLoggedInFlow: Flow<Boolean> = token.map { it != null }
    suspend fun isLoggedInBlocking(): Boolean {
        return token.firstOrNull() != null
    }

    suspend fun clearUserSession() {
        appDataStore.edit { settings ->
            settings.remove(UserPreferencesKeys.TOKEN)
            settings.remove(UserPreferencesKeys.NAME)
            settings.remove(UserPreferencesKeys.EMAIL)
        }
        cachedToken = null
    }

    suspend fun clear() {
        appDataStore.edit { settings ->
            settings.clear()
        }
    }

    fun getCachedToken(): String? = cachedToken

    val syncTimeFlow: Flow<Pair<Int, Int>> = appDataStore.data
        .map { preferences ->
            val hour =
                preferences[UserPreferencesKeys.SYNC_HOUR] ?: UserPreferencesKeys.DEFAULT_SYNC_HOUR
            val minute = preferences[UserPreferencesKeys.SYNC_MINUTE]
                ?: UserPreferencesKeys.DEFAULT_SYNC_MINUTE
            Pair(hour, minute)
        }

    suspend fun saveSyncTime(hour: Int, minute: Int) {
        appDataStore.edit { settings ->
            settings[UserPreferencesKeys.SYNC_HOUR] = hour
            settings[UserPreferencesKeys.SYNC_MINUTE] = minute
        }
    }
}

object UserPreferencesKeys {
    // User Session Keys
    val TOKEN = stringPreferencesKey("user_token")
    val NAME = stringPreferencesKey("user_name")
    val EMAIL = stringPreferencesKey("user_email")

    // Settings Keys
    val DAILY_SYNC = booleanPreferencesKey("daily_sync")
    val MONTH_REFLECTION_DATE = intPreferencesKey("month_reflection_date")
    val MONTH_REFLECTION_NOTIFICATION = booleanPreferencesKey("month_reflection_notification")

    // Sync Time Keys
    val SYNC_HOUR = intPreferencesKey("sync_schedule_hour")
    val SYNC_MINUTE = intPreferencesKey("sync_schedule_minute")

    // Default Sync Time Values
    const val DEFAULT_SYNC_HOUR = 2 // 02:00 AM
    const val DEFAULT_SYNC_MINUTE = 0
}

data class UserSession(
    val name: String? = null,
    val email: String? = null
)

data class UserSettings(
    val isDailySync: Boolean = false,
    val monthReflectionDate: Int = 1,
    val isMonthlyReflectNotification: Boolean = true,
)

