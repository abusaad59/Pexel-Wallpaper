package com.app.pexelwallpaper

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.pexelwallpaper.adapter.CategoriesAdapter
import com.app.pexelwallpaper.adapter.WallpaperAdapter
import com.app.pexelwallpaper.datamodels.Category
import com.app.pexelwallpaper.repository.PexelRepository
import com.app.pexelwallpaper.retrofit.RetrofitService
import com.app.pexelwallpaper.ui.CategoryWallpaperActivity
import com.app.pexelwallpaper.viewmodel.PexelViewModelFactory
import com.app.pexelwallpaper.viewmodel.PexelsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: PexelsViewModel
    private val retrofitService: RetrofitService = RetrofitService.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var wallpaperRecyclerView: RecyclerView
    private lateinit var progressBar:ProgressBar
    private lateinit var myFavourites:TextView
    private lateinit var ivMenu:ImageView
    private lateinit var drawer:DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(
            this,
            PexelViewModelFactory(PexelRepository(retrofitService))
        )[PexelsViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView=findViewById(R.id.rvCategories)
        wallpaperRecyclerView=findViewById(R.id.rvTrending)
        progressBar=findViewById(R.id.progressBar)
        myFavourites=findViewById(R.id.myFav)
        ivMenu=findViewById(R.id.ivMenu)
        drawer=findViewById(R.id.drawer)
        viewModel.getTrendingImages()

        ivMenu.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
        val adapter=CategoriesAdapter(populateCategories(),this)
        recyclerView.adapter=adapter

        viewModel.wallpaperList.observe(this) {
            if (it != null) {
                progressBar.visibility=View.GONE
                val wallpaperAdapter = WallpaperAdapter(it, context = this)
                wallpaperRecyclerView.adapter = wallpaperAdapter
            }
        }

        myFavourites.setOnClickListener {
            startActivity(Intent(this,CategoryWallpaperActivity::class.java).putExtra("name","Favourites"))
            drawer.closeDrawer(GravityCompat.START)

        }
    }

    private fun populateCategories(): ArrayList<Category> {
        val categories = ArrayList<Category>()
        categories.add(Category("Nature", R.drawable.nature))
        categories.add(Category("Flower", R.drawable.flower))
        categories.add(Category("Mountain", R.drawable.mountain))
        categories.add(Category("Music", R.drawable.music))
        categories.add(Category("Sports", R.drawable.sports))
        categories.add(Category("Animal", R.drawable.animal))
        return categories
    }
}