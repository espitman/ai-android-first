package com.example.hello.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hello.R
import com.example.hello.data.models.CategoryItem

class CategoriesAdapter(private val items: List<CategoryItem>) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconView: ImageView = view.findViewById(R.id.categoryIcon)
        val titleView: TextView = view.findViewById(R.id.categoryTitle)
        val badgeContainer: CardView = view.findViewById(R.id.categoryBadgeContainer)
        val badgeText: TextView = view.findViewById(R.id.categoryBadgeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]
        holder.titleView.text = item.title
        
        // Load Icon
        holder.iconView.load(item.icon) {
            crossfade(true)
            placeholder(R.drawable.rounded_corner_bg)
            error(R.drawable.rounded_corner_bg)
        }

        // Handle Badge
        if (item.badge != null) {
            holder.badgeContainer.visibility = View.VISIBLE
            holder.badgeText.text = item.badge.title
            
            try {
                // Parse Colors (defaulting if invalid)
                val bgColor = item.badge.bgColor?.let { Color.parseColor(it) } ?: Color.RED
                val textColor = item.badge.textColor?.let { Color.parseColor(it) } ?: Color.WHITE
                
                holder.badgeContainer.setCardBackgroundColor(bgColor)
                holder.badgeText.setTextColor(textColor)
            } catch (e: Exception) {
                // Fallback colors
                holder.badgeContainer.setCardBackgroundColor(Color.RED)
                holder.badgeText.setTextColor(Color.WHITE)
            }
        } else {
            holder.badgeContainer.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size
}
