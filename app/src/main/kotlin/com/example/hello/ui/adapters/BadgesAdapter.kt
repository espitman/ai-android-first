package com.example.hello.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hello.R
import com.example.hello.data.models.BadgeItem

class BadgesAdapter(private val items: List<BadgeItem>) : RecyclerView.Adapter<BadgesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.badgeTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_badge_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        
        // Handle Item Colors
        val bgDrawable = holder.title.background.mutate() as? android.graphics.drawable.GradientDrawable
        if (bgDrawable != null) {
            item.colors?.background?.let {
                try { 
                    bgDrawable.setColor(android.graphics.Color.parseColor(it)) 
                } catch (e: Exception) {
                    bgDrawable.setColor(android.graphics.Color.LTGRAY)
                }
            }
            bgDrawable.setStroke(0, 0) // Explicitly remove any stroke
        }
        
        item.colors?.text?.let {
            try { 
                holder.title.setTextColor(android.graphics.Color.parseColor(it)) 
            } catch (e: Exception) {
                holder.title.setTextColor(android.graphics.Color.BLACK)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
