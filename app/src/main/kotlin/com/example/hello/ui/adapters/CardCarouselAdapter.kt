package com.example.hello.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hello.R
import com.example.hello.data.models.CardCarouselItem

class CardCarouselAdapter(private val items: List<CardCarouselItem>) : RecyclerView.Adapter<CardCarouselAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.itemImage)
        val title: TextView = view.findViewById(R.id.itemTitle)
        val subTitle: TextView = view.findViewById(R.id.itemSubTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_carousel_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.subTitle.text = item.subTitle
        holder.subTitle.visibility = if (item.subTitle.isNullOrEmpty()) View.GONE else View.VISIBLE
        
        holder.image.load(item.image) {
            crossfade(true)
            placeholder(R.color.gray_placeholder)
        }
    }

    override fun getItemCount(): Int = items.size
}
