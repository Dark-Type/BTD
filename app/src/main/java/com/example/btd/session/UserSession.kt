package com.example.btd.session

import android.content.Context
import android.content.SharedPreferences

object UserSession {
    private const val PREF_NAME = "user_session"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_ROLE = "user_role"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) {
            prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
        }

    var userRole: String
        get() = prefs.getString(KEY_USER_ROLE, "") ?: ""
        set(value) {
            prefs.edit().putString(KEY_USER_ROLE, value).apply()
        }

    fun logout() {
            prefs.edit().clear().apply()
        isLoggedIn = false
        userRole = ""
    }
}