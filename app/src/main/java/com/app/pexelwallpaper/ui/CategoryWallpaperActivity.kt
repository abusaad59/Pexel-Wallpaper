package com.app.pexelwallpaper.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pexelwallpaper.R
import com.app.pexelwallpaper.adapter.FavouriteAdapter
import com.app.pexelwallpaper.adapter.WallpaperAdapter
import com.app.pexelwallpaper.repository.PexelRepository
import com.app.pexelwallpaper.retrofit.RetrofitService
import com.app.pexelwallpaper.viewmodel.PexelViewModelFactory
import com.app.pexelwallpaper.viewmodel.PexelsViewModel
import com.app.pexelwallpaper.viewmodel.RoomViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class CategoryWallpaperActivity : AppCompatActivity() {
    private lateinit var wallpaperRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: PexelsViewModel
    private lateinit var tvName:TextView
    private lateinit var ivBack:ImageView
    private val retrofitService: RetrofitService = RetrofitService.getInstance()
    private lateinit var roomViewModel: RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_wallpaper)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(
            this,
            PexelViewModelFactory(PexelRepository(retrofitService))
        )[PexelsViewModel::class.java]
        roomViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[RoomViewModel::class.java]

        wallpaperRecyclerView=findViewById(R.id.rvTrending)
        progressBar=findViewById(R.id.progressBar)
        tvName=findViewById(R.id.tvName)
        ivBack=findViewById(R.id.ivBack)

        ivBack.setOnClickListener {
            onBackPressed()
        }
        val nameByIntent=intent.getStringExtra("name")
        tvName.text=nameByIntent

        if (nameByIntent!="Favourites"){
            viewModel.getImages(nameByIntent?.toLowerCase().toString())
            val layoutManager=GridLayoutManager(this,2)
            viewModel.wallpaperList.observe(this) {
                if (it != null) {
                    progressBar.visibility= View.GONE
                    val wallpaperAdapter = WallpaperAdapter(it, context = this)
                    wallpaperRecyclerView.layoutManager=layoutManager
                    wallpaperRecyclerView.adapter = wallpaperAdapter
                }
            }
        }else {
            val layoutManager=GridLayoutManager(this,2)
            roomViewModel.favouriteList.observe(this) {
                progressBar.visibility= View.GONE
                if (it!=null){
                    val favAdapter=FavouriteAdapter(it.distinctBy { it.imageUrl },this)
                    wallpaperRecyclerView.layoutManager=layoutManager
                    wallpaperRecyclerView.adapter=favAdapter
                }
            }
        }


    }
}