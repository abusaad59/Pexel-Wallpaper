package com.app.pexelwallpaper.ui

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.app.pexelwallpaper.R
import com.app.pexelwallpaper.response.Photo
import com.app.pexelwallpaper.room.Favourite
import com.app.pexelwallpaper.viewmodel.RoomViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class WallpaperViewActivity : AppCompatActivity() {
    private lateinit var ivMainImage: ImageView
    private lateinit var ivShare: ImageView
    private lateinit var ivWallpaper: ImageView
    private lateinit var ivDownload: ImageView
    private lateinit var ivFavourite: ImageView
    private lateinit var ivBack: ImageView
    private lateinit var tvName:TextView
    private var bitmap: Bitmap? = null
    private var isAlreadyAdded = 0
    private lateinit var viewModel: RoomViewModel
    private var imageUrl:String=""
    private var photographer:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wallpaper_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[RoomViewModel::class.java]

        imageUrl= intent.getStringExtra("wallpaperUrl").toString()
        photographer= intent.getStringExtra("photographer").toString()
        ivShare = findViewById(R.id.ivShare)
        ivWallpaper = findViewById(R.id.ivWallpaper)
        ivDownload = findViewById(R.id.ivDownload)
        ivFavourite = findViewById(R.id.ivFavourite)
        ivMainImage = findViewById(R.id.ivMainImage)
        ivBack = findViewById(R.id.ivBack)
        tvName=findViewById(R.id.tvName)

        tvName.text=photographer
        checkIfAlreadyExist()
        ivFavourite.setOnClickListener {
            if (isAlreadyAdded == 0) {
                val favourite = Favourite(imageUrl = imageUrl ?: "", photographer = photographer)
                viewModel.insertFavourite(favourite)
                isAlreadyAdded=1
                ivFavourite.setImageResource(R.drawable.ic_favourite_filled)
            } else {
                viewModel.deleteFavourite(imageUrl ?: "")
                isAlreadyAdded=0
                ivFavourite.setImageResource(R.drawable.ic_favourite)
            }
        }
        ivDownload.setOnClickListener {
            if (bitmap != null) {
                saveImageToGallery(bitmap!!)
            }
        }
        ivShare.setOnClickListener {
            if (bitmap != null) {
                shareImage(bitmap!!)
            }
        }
        ivWallpaper.setOnClickListener {
            if (bitmap != null) {
                setImageWallpaper(bitmap!!)
            }
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }

        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                    ivMainImage.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle placeholder if necessary
                }
            })
    }

    private fun checkIfAlreadyExist() {
        viewModel.checkFavouriteExists(imageUrl) { exists ->
            Log.d("checkIfAlreadyExist", "checkIfAlreadyExist: $exists")
            isAlreadyAdded = exists
            if (exists == 0) {
                ivFavourite.setImageResource(R.drawable.ic_favourite)
            } else {
                ivFavourite.setImageResource(R.drawable.ic_favourite_filled)
            }
        }
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/PexelWallpaper"
            )
        }

        val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            try {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun shareImage(bitmap: Bitmap) {
        try {
            val cachePath = File(externalCacheDir, "my_images/")
            cachePath.mkdirs()
            val file = File(cachePath, "shared_image.jpg")

            FileOutputStream(file).use { fileOutputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            }

            val contentUri: Uri =
                FileProvider.getUriForFile(this, "com.app.pexelwallpaper.fileprovider", file)

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_STREAM, contentUri)
                type = "image/jpeg"
            }

            startActivity(Intent.createChooser(shareIntent, "Share image via"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setImageWallpaper(bitmap: Bitmap) {
        WallpaperManager.getInstance(this).apply {
            try {
                setBitmap(bitmap)
                Toast.makeText(
                    this@WallpaperViewActivity,
                    "Wallpaper set successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: IOException) {
                Toast.makeText(
                    this@WallpaperViewActivity,
                    "Failed to set wallpaper",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }

}