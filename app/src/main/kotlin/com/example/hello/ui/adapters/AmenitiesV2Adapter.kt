package com.example.hello.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.decode.SvgDecoder
import com.example.hello.R
import com.example.hello.data.models.AmenityV2

class AmenitiesV2Adapter(private val categories: List<com.example.hello.data.models.AmenityCategory>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    private val flatItems = mutableListOf<Pair<Int, com.example.hello.data.models.AmenityV2>>()

    init {
        categories.forEach { category ->
            // Use dummy AmenityV2 for header title
            val headerItem = com.example.hello.data.models.AmenityV2(title = category.title)
            flatItems.add(TYPE_HEADER to headerItem)
            category.items?.forEach { item ->
                flatItems.add(TYPE_ITEM to item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = flatItems[position].first

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_amenity_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_amenity_v2, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (type, item) = flatItems[position]
        if (type == TYPE_HEADER) {
            (holder as HeaderViewHolder).title.text = item.title?.fa
        } else {
            val h = holder as ItemViewHolder
            val isAvailable = item.state == true
            
            h.title.text = item.title?.fa
            h.title.setTextColor(if (isAvailable) Color.parseColor("#333333") else Color.parseColor("#BBBBBB"))
            
            h.icon.load(item.icon?.url) {
                decoderFactory(SvgDecoder.Factory())
            }
            
            if (!isAvailable) {
                h.state.visibility = View.VISIBLE
                h.state.setImageResource(R.drawable.ic_close_grey)
                h.icon.alpha = 0.4f
                h.title.setTextColor(Color.parseColor("#BBBBBB"))
            } else {
                h.state.visibility = View.GONE
                h.icon.alpha = 1.0f
                h.title.setTextColor(Color.parseColor("#333333"))
            }
            
            val subText = item.subItems?.joinToString(" . ")
            if (!subText.isNullOrEmpty()) {
                h.subTitle.visibility = View.VISIBLE
                h.subTitle.text = subText
            } else {
                h.subTitle.visibility = View.GONE
            }
            
            h.divider.visibility = if (position < flatItems.size - 1 && flatItems[position+1].first == TYPE_ITEM) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount() = flatItems.size

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvHeaderTitle)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.ivAmenityIcon)
        val title: TextView = view.findViewById(R.id.tvAmenityTitle)
        val subTitle: TextView = view.findViewById(R.id.tvAmenitySubTitle)
        val state: ImageView = view.findViewById(R.id.ivStateIcon)
        val divider: View = view.findViewById(R.id.divider)
    }
}
