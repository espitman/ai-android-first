package com.example.hello.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.hello.R
import com.example.hello.data.models.Widget

class MainAdapter(private val widgets: List<Widget>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SLIDER = 1
        const val TYPE_CATEGORIES = 2
        const val TYPE_BANNER = 3
        const val TYPE_ACCOMMODATION = 4
        const val TYPE_UNKNOWN = 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (widgets[position].type) {
            "slider" -> TYPE_SLIDER
            "categories" -> TYPE_CATEGORIES
            "banner" -> TYPE_BANNER
            "accommodationCarousel" -> TYPE_ACCOMMODATION
            else -> TYPE_UNKNOWN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SLIDER -> SliderViewHolder(inflater.inflate(R.layout.item_widget_slider, parent, false))
            TYPE_CATEGORIES -> CategoriesViewHolder(inflater.inflate(R.layout.item_widget_categories, parent, false))
            TYPE_BANNER -> BannerViewHolder(inflater.inflate(R.layout.item_widget_banner, parent, false))
            TYPE_ACCOMMODATION -> AccommodationViewHolder(inflater.inflate(R.layout.item_widget_accommodation, parent, false))
            else -> UnknownViewHolder(View(parent.context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val widget = widgets[position]
        when (holder) {
            is SliderViewHolder -> {
                val items = widget.slider?.items ?: emptyList()
                holder.viewPager.adapter = SliderAdapter(items)
            }
            is CategoriesViewHolder -> {
                val items = widget.categories?.items ?: emptyList()
                holder.recyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 4)
                holder.recyclerView.adapter = CategoriesAdapter(items)
            }
            is BannerViewHolder -> {
                val banner = widget.banner
                if (banner != null) {
                    holder.imageView.load(banner.gifUrl)
                }
            }
            is AccommodationViewHolder -> {
                val acc = widget.accommodationCarousel
                if (acc != null) {
                    holder.title.text = acc.title
                    holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                    holder.recyclerView.adapter = AccommodationAdapter(acc.items)
                }
            }
        }
    }

    override fun getItemCount(): Int = widgets.size

    class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPagerSlider)
    }

    class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView = view.findViewById(R.id.rvCategories)
    }

    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.ivBanner)
    }

    class AccommodationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvAccTitle)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvAccommodations)
    }

    class UnknownViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
