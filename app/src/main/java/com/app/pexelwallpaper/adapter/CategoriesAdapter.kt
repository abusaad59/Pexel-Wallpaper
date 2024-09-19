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
import com.app.pexelwallpaper.ui.CategoryWallpaperActivity

class CategoriesAdapter(
    private val model: List<Category>,
    val context: Context
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = model[position]
        holder.tvName.text = model.name
        holder.ivImage.setImageResource(model.icon)

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,CategoryWallpaperActivity::class.java).putExtra("name",model.name))
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)

    }
}
