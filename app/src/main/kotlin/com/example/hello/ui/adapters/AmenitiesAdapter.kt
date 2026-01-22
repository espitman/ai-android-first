package com.example.hello.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.decode.SvgDecoder
import com.example.hello.R
import com.example.hello.data.models.Amenity

class AmenitiesAdapter(private val items: List<Amenity>) :
    RecyclerView.Adapter<AmenitiesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.ivAmenityIcon)
        val title: TextView = view.findViewById(R.id.tvAmenityTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_amenity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title?.fa
        
        holder.icon.load(item.icon?.url) {
            crossfade(true)
            decoderFactory(SvgDecoder.Factory())
        }
    }

    override fun getItemCount() = items.size
}
