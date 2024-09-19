package com.app.pexelwallpaper.repository

import androidx.lifecycle.LiveData
import com.app.pexelwallpaper.room.Favourite
import com.app.pexelwallpaper.room.FavouriteDao


class RoomRepository constructor(private val favouriteDao: FavouriteDao) {
    val allFavourites: LiveData<List<Favourite>> = favouriteDao.getData()
    fun insertFavourite(favourite: Favourite) {
        favouriteDao.insert(favourite)
    }
    suspend fun deleteFromFavourite(imageUrl:String): Int {
        return favouriteDao.deleteByUrl(imageUrl)
    }

    suspend fun checkIfAlreadyExists(imageUrl:String):Int{
        return favouriteDao.existsByUrl(imageUrl)
    }
}