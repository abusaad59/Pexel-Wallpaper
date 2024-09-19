package com.app.pexelwallpaper.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.pexelwallpaper.repository.PexelRepository
import com.app.pexelwallpaper.response.PexelsResponse
import com.app.pexelwallpaper.response.Photo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PexelsViewModel(private val repository: PexelRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val wallpaperList = MutableLiveData<List<Photo>>()

    fun getImages(category:String) {
        val response = repository.getImages(category)
            response.enqueue(object : Callback<PexelsResponse> {
                override fun onResponse(
                    call: Call<PexelsResponse>,
                    response: Response<PexelsResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            wallpaperList.postValue(response.body()?.photos)
                            Log.d("checkresponse", "onResponse"+response.body())
                        }
                    }
                }

                override fun onFailure(call: Call<PexelsResponse>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
    }

    fun getTrendingImages() {
        val response = repository.getTrendingWallpapers()
        response.enqueue(object : Callback<PexelsResponse> {
            override fun onResponse(
                call: Call<PexelsResponse>,
                response: Response<PexelsResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        wallpaperList.postValue(response.body()?.photos)
                        Log.d("checkresponse", "onResponse"+response.body())
                    }
                }
            }

            override fun onFailure(call: Call<PexelsResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}

