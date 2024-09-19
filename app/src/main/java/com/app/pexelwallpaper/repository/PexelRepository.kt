package com.app.pexelwallpaper.repository

import com.app.pexelwallpaper.retrofit.RetrofitService


class PexelRepository(private val retrofitService: RetrofitService) {
    fun getImages(query:String) = retrofitService.searchPhotos(query)
    fun getTrendingWallpapers() = retrofitService.trendingWallpapers()

}