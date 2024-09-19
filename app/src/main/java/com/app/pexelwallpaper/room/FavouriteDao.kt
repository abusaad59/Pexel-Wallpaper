package com.app.pexelwallpaper.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favourite: Favourite)

    @Query("Select * from wallpapers order by id ASC")
    fun getData(): LiveData<List<Favourite>>

    @Query("DELETE FROM wallpapers WHERE imageUrl = :url")
    suspend fun deleteByUrl(url: String): Int

    @Query("SELECT COUNT(*) > 0 FROM wallpapers WHERE imageUrl = :url")
    suspend fun existsByUrl(url: String): Int

}