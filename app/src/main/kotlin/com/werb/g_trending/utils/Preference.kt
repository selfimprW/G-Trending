package com.werb.g_trending.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson

/** Created by wanbo <werbhelius@gmail.com> on 2017/9/10. */

object Preference {

    private val THEME = "theme"
    private val LANGUAGE = "language"

    private fun getSharedPreferences(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun setTheme(context: Context, theme: Theme) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(THEME, theme.name)
        editor.apply()
    }

    fun getTheme(context: Context): Theme = Theme.valueOf(getSharedPreferences(context).getString(THEME, Theme.Default.name))

    fun setLanguage(context: Context, languages: MutableList<String>){
        val editor = getSharedPreferences(context).edit()
        editor.putString(LANGUAGE, Gson().toJson(languages))
        editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun getLanguage(context: Context):  MutableList<String> {
        val str = getSharedPreferences(context).getString(LANGUAGE, "")
        val fromJson = Gson().fromJson(str, MutableList::class.java)
        return fromJson?.let { it } as MutableList<String>? ?: mutableListOf()
    }

}