package com.app.pexelwallpaper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.pexelwallpaper.repository.RoomRepository
import com.app.pexelwallpaper.room.Favourite
import com.app.pexelwallpaper.room.FavouriteDatabase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {
    val favouriteList: LiveData<List<Favourite>>
    private val repository: RoomRepository

    init {
        val favouriteDao = FavouriteDatabase.getDatabase(application).getAllFavourite()
        repository = RoomRepository(favouriteDao)
        favouriteList = repository.allFavourites
    }

    fun insertFavourite(favourite: Favourite) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFavourite(favourite)
    }

    fun deleteFavourite(imageUrl:String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFromFavourite(imageUrl)
    }

    fun checkFavouriteExists(url: String, callback: (Int) -> Unit) {
        viewModelScope.launch {
            val exists = repository.checkIfAlreadyExists(url)
            callback(exists)
        }
    }
}