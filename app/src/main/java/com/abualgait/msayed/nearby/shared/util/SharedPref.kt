package com.abualgait.msayed.nearby.shared.util

import android.content.SharedPreferences
import com.abualgait.msayed.nearby.shared.enums.OperationalMode


class SharedPref(private val pref: SharedPreferences) {

    private var editor: SharedPreferences.Editor = pref.edit()

    private fun putString(value: String?, key: Key) {
        editor.putString(key.name, value)
        editor.apply()
    }

    private fun getString(key: Key, def: String): String? {
        return pref.getString(key.name, def)
    }


    fun clear() {
        editor.clear()
        editor.apply()
    }

    enum class Key {
        MODE,
        LAT, LON

    }


    var mode: String
        get() = getString(Key.MODE, OperationalMode.REALTIME.name)!!
        set(value) {
            putString(value, Key.MODE)
        }

    var longitude: String
        get() = getString(
            Key.LON,
            "31.23944"
        )!!// i set this as default value not (0.0) cause foursquare api restrict 0.0 point.
        set(value) {
            putString(value, Key.LON)
        }

    var latitude: String
        get() = getString(Key.LAT, "30.05611")!!
        set(value) {
            putString(value, Key.LAT)
        }


}
