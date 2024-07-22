package com.example.mileageplanner.data

import android.content.Context

class PreferencesRepository (private val context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }
}
