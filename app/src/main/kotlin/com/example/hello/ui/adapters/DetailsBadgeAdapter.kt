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
import com.example.hello.data.models.BadgeDetail

class DetailsBadgeAdapter(private val items: List<BadgeDetail>) : RecyclerView.Adapter<DetailsBadgeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.ivBadgeIcon)
        val title: TextView = view.findViewById(R.id.tvBadgeTitle)
        val description: TextView = view.findViewById(R.id.tvBadgeDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_details_badge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        
        val sub = item.description ?: item.subTitle ?: item.subText ?: item.subtitle_lower
        
        val dataContainer = holder.itemView.findViewById<android.widget.LinearLayout>(R.id.dataContainer)
        dataContainer.removeAllViews()
        
        if (item.data.isNullOrEmpty()) {
            holder.description.text = sub
            holder.description.visibility = if (sub.isNullOrEmpty()) View.GONE else View.VISIBLE
            dataContainer.visibility = View.GONE
        } else if (item.data.size == 1) {
            val combined = if (sub.isNullOrEmpty()) item.data[0] else "$sub - ${item.data[0]}"
            holder.description.text = combined
            holder.description.visibility = View.VISIBLE
            dataContainer.visibility = View.GONE
        } else {
            holder.description.text = sub
            holder.description.visibility = if (sub.isNullOrEmpty()) View.GONE else View.VISIBLE
            dataContainer.visibility = View.VISIBLE
            
            item.data.forEach { text ->
                val textView = TextView(holder.itemView.context).apply {
                    this.text = "• $text"
                    this.textSize = 13f
                    this.setTextColor(android.graphics.Color.parseColor("#777777"))
                    this.setPadding(0, 4, 0, 4)
                }
                dataContainer.addView(textView)
            }
        }
        
        holder.icon.load(item.icon) {
            crossfade(true)
        }
    }

    override fun getItemCount(): Int = items.size
}
