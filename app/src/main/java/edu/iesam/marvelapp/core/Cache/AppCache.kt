package edu.iesam.marvelapp.core.Cache

import android.content.Context
import edu.iesam.marvelapp.R
import androidx.core.content.edit
import edu.iesam.marvelapp.core.Time.AppTime

interface AppCache{
    fun updateTime(key: String)
    fun isExpired(key: String, ttl: Long): Boolean
}

class AppCacheTTL(
    private val context: Context,
    private val appTime: AppTime
): AppCache {

    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.app_cache), Context.MODE_PRIVATE)

    override fun updateTime(key: String){
        sharedPref.edit {
            putLong(key, appTime.currentTimeToLong())
            apply()
        }
    }
    override fun isExpired(key: String, ttl: Long): Boolean{
        val timeSaved = sharedPref.getLong(key, -1)

        return (appTime.currentTimeToLong() - timeSaved) >= ttl
    }
}