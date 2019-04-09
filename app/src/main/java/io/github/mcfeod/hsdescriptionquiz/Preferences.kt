package io.github.mcfeod.hsdescriptionquiz

import android.content.Context
import android.content.SharedPreferences


class Preferences (context: Context) {
    companion object {
        const val PREFS_FILENAME: String = "io.github.mcfeod.hsdescriptionquiz.prefs"
        const val LOCALE = "locale"
        const val ITEM_COUNT = "item_count"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var itemCount: Int
        get() = prefs.getInt(ITEM_COUNT, 10)
        set(value) = prefs.edit().putInt(ITEM_COUNT, value).apply()

    var locale: String
        get() = prefs.getString(LOCALE, "ruRU") as String
        set(value) = prefs.edit().putString(LOCALE, value).apply()
}