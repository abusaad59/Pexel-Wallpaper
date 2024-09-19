package com.app.pexelwallpaper.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers")
class Favourite(
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "photographer") val photographer: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}