package com.example.hello.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hello.R
import com.example.hello.data.models.AccommodationItem
import java.text.NumberFormat
import java.util.Locale

class AccommodationAdapter(private val items: List<AccommodationItem>) : RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder>() {

    class AccommodationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.accImage)
        val title: TextView = view.findViewById(R.id.accTitle)
        val location: TextView = view.findViewById(R.id.accLocation)
        val price: TextView = view.findViewById(R.id.accPrice)
        val discountBadge: TextView = view.findViewById(R.id.accDiscountBadge)
        val score: TextView = view.findViewById(R.id.accScore)
        val scoreCount: TextView = view.findViewById(R.id.accScoreCount)
        val capacity: TextView = view.findViewById(R.id.accCapacity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccommodationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_accommodation, parent, false)
        return AccommodationViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccommodationViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.location.text = "${item.province}، ${item.city}"
        
        // Final Price (Always show only this)
        val finalPriceValue = item.price?.discounted ?: item.price?.full ?: 0
        holder.price.text = NumberFormat.getNumberInstance(Locale.US).format(finalPriceValue)
        
        // Handle Discount Badge visibility only
        if (item.price != null && item.price.discountPercentage > 0) {
            holder.discountBadge.visibility = View.VISIBLE
            holder.discountBadge.text = "${item.price.discountPercentage}٪ تخفیف"
        } else {
            holder.discountBadge.visibility = View.GONE
        }
        
        // Stats
        holder.score.text = NumberFormat.getNumberInstance(Locale.US).format(item.rate?.score ?: 0.0)
        holder.scoreCount.text = "(${item.rate?.count ?: 0} نظر)"
        holder.capacity.text = "${item.roomsCount} نفر"
        
        val imageUrl = item.images.firstOrNull() ?: ""
        holder.image.load(imageUrl) {
            crossfade(true)
            placeholder(R.color.gray_placeholder)
            error(R.color.gray_placeholder)
        }

        // Set transition names for shared element animation
        holder.image.transitionName = "shared_image_${item.id}"
        holder.title.transitionName = "shared_title_${item.id}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            if (context is android.app.Activity) {
                val intent = android.content.Intent(context, com.example.hello.ui.AccommodationDetailActivity::class.java).apply {
                    putExtra("ACCOMMODATION_CODE", item.id)
                    putExtra("ACCOMMODATION_TITLE", item.title)
                    putExtra("ACCOMMODATION_IMAGE", imageUrl)
                }

                val options = androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context,
                    androidx.core.util.Pair(holder.image, "shared_image"),
                    androidx.core.util.Pair(holder.title, "shared_title")
                )
                context.startActivity(intent, options.toBundle())
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
