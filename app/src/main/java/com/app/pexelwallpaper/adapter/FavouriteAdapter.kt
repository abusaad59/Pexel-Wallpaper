package com.app.pexelwallpaper.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.pexelwallpaper.R
import com.app.pexelwallpaper.datamodels.Category
import com.app.pexelwallpaper.response.Photo
import com.app.pexelwallpaper.room.Favourite
import com.app.pexelwallpaper.ui.WallpaperViewActivity
import com.bumptech.glide.Glide

class FavouriteAdapter(
    private val model: List<Favourite>,
    val context: Context
) :
    RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_favourites, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = model[position]
        Glide.with(context).load(model.imageUrl).into(holder.ivImage)
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, WallpaperViewActivity::class.java).putExtra("wallpaperUrl",model.imageUrl).putExtra("photographer",model.photographer))
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivWallpaper)

    }
}
