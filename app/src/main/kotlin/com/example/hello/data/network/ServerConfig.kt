package com.example.hello.data.network

import android.content.Context
import android.content.SharedPreferences

object ServerConfig {
    private const val PREFS_NAME = "app_settings"
    private const val KEY_SERVER_URL = "server_url"
    
    const val URL_LOCAL = "http://192.168.1.7:5176/"
    const val URL_VERCEL = "https://ai-jabama-home-git-main-espitmans-projects.vercel.app/"
    
    private var prefs: SharedPreferences? = null
    
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun getServerUrl(): String {
        return prefs?.getString(KEY_SERVER_URL, URL_LOCAL) ?: URL_LOCAL
    }
    
    fun setServerUrl(url: String) {
        prefs?.edit()?.putString(KEY_SERVER_URL, url)?.apply()
    }
    
    fun isLocalSelected(): Boolean {
        return getServerUrl() == URL_LOCAL
    }
}
