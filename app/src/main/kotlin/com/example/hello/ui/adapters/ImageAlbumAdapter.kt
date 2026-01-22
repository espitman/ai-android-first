package com.example.hello.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hello.R

class ImageAlbumAdapter(
    private val imageUrls: List<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ImageAlbumAdapter.ImageViewHolder>() {

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.ivPagerImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_pager, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.load(imageUrls[position]) {
            crossfade(true)
            placeholder(R.color.gray_placeholder)
            error(R.color.gray_placeholder)
        }
        holder.imageView.setOnClickListener { onItemClick(position) }
    }

    override fun getItemCount(): Int = imageUrls.size
}
