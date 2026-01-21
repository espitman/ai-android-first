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
        const val TYPE_LINK_CAROUSEL = 5
        const val TYPE_UNKNOWN = 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (widgets[position].type) {
            "slider" -> TYPE_SLIDER
            "categories" -> TYPE_CATEGORIES
            "banner" -> TYPE_BANNER
            "accommodationCarousel" -> TYPE_ACCOMMODATION
            "linkCarousel" -> TYPE_LINK_CAROUSEL
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
            TYPE_LINK_CAROUSEL -> LinkCarouselViewHolder(inflater.inflate(R.layout.item_widget_link_carousel, parent, false))
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
            is LinkCarouselViewHolder -> {
                val data = widget.linkCarousel
                if (data != null) {
                    holder.title.text = data.title
                    holder.subTitle.text = data.subTitle
                    holder.subTitle.visibility = if (data.subTitle.isNullOrEmpty()) View.GONE else View.VISIBLE
                    
                    data.colors?.background?.let {
                        try { holder.container.setBackgroundColor(android.graphics.Color.parseColor(it)) } catch (e: Exception) {}
                    }
                    data.colors?.title?.let {
                        try { holder.title.setTextColor(android.graphics.Color.parseColor(it)) } catch (e: Exception) {}
                    }
                    data.colors?.subTitle?.let {
                        try { holder.subTitle.setTextColor(android.graphics.Color.parseColor(it)) } catch (e: Exception) {}
                    }

                    holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                    holder.recyclerView.adapter = LinkCarouselAdapter(data.items)
                }
            }
            is AccommodationViewHolder -> {
                val acc = widget.accommodationCarousel
                if (acc != null) {
                    holder.title.text = acc.title
                    
                    // Setup Button
                    val btn = acc.button
                    if (btn != null) {
                        holder.btnShowAll.visibility = View.VISIBLE
                        holder.btnShowAll.text = btn.text
                        
                        btn.colors?.text?.let {
                            try { holder.btnShowAll.setTextColor(android.graphics.Color.parseColor(it)) } catch (e: Exception) {}
                        }
                        
                        // Handle dynamic background and border
                        val bgDrawable = holder.btnShowAll.background as? android.graphics.drawable.GradientDrawable
                        if (bgDrawable != null) {
                            btn.colors?.background?.let {
                                try { bgDrawable.setColor(android.graphics.Color.parseColor(it)) } catch (e: Exception) {}
                            }
                            btn.colors?.border?.let {
                                try { bgDrawable.setStroke(3, android.graphics.Color.parseColor(it)) } catch (e: Exception) {}
                            }
                        }
                    } else {
                        holder.btnShowAll.visibility = View.GONE
                    }

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

    class LinkCarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: View = view.findViewById(R.id.container)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val subTitle: TextView = view.findViewById(R.id.tvSubTitle)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvItems)
    }

    class AccommodationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvAccTitle)
        val btnShowAll: TextView = view.findViewById(R.id.btnShowAll)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvAccommodations)
    }

    class UnknownViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
