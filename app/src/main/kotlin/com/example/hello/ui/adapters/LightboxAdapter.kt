package com.example.hello.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hello.R
import com.github.chrisbanes.photoview.PhotoView

class LightboxAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<LightboxAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoView: PhotoView = view.findViewById(R.id.photoView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lightbox_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.photoView.load(imageUrls[position]) {
            crossfade(true)
        }
    }

    override fun getItemCount(): Int = imageUrls.size
}
