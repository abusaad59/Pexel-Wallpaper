package com.app.pexelwallpaper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.pexelwallpaper.repository.PexelRepository

class PexelViewModelFactory constructor(private val repository: PexelRepository) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(PexelsViewModel::class.java)) {
            PexelsViewModel(this.repository) as T
        }
        else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}