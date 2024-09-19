package com.app.pexelwallpaper.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Favourite::class],
    version = 2,
    exportSchema = false
)
abstract class FavouriteDatabase : RoomDatabase() {
    abstract fun getAllFavourite(): FavouriteDao


    companion object {
        @Volatile
        private var INSTANCE: FavouriteDatabase? = null
        fun getDatabase(context: Context): FavouriteDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, FavouriteDatabase::class.java, "favourite_pexels_db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
