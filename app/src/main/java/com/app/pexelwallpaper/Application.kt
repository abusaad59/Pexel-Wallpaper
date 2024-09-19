package com.app.pexelwallpaper

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.app.pexelwallpaper.room.FavouriteDatabase

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        favouriteDatabase = FavouriteDatabase.getDatabase(this)

    }
    companion object {
        var favouriteDatabase:FavouriteDatabase? = null
    }
}