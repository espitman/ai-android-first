package com.example.hello.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hello.R
import com.example.hello.data.models.ReviewV2
import com.example.hello.utils.NumberUtils

class ReviewsVerticalAdapter(private val reviews: MutableList<ReviewV2>) :
    RecyclerView.Adapter<ReviewsVerticalAdapter.ReviewViewHolder>() {

    fun addReviews(newReviews: List<ReviewV2>) {
        val startPos = reviews.size
        reviews.addAll(newReviews)
        notifyItemRangeInserted(startPos, newReviews.size)
    }

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivUserAvatar: ImageView = view.findViewById(R.id.ivUserAvatar)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvStayDate: TextView = view.findViewById(R.id.tvStayDate)
        val tvStayDuration: TextView = view.findViewById(R.id.tvStayDuration)
        val tvComment: TextView = view.findViewById(R.id.tvComment)
        val llStars: LinearLayout = view.findViewById(R.id.llStars)
        
        // Host response
        val llHostResponse: View = view.findViewById(R.id.llHostResponse)
        val ivHostAvatar: ImageView = view.findViewById(R.id.ivHostAvatar)
        val tvHostName: TextView = view.findViewById(R.id.tvHostName)
        val tvHostComment: TextView = view.findViewById(R.id.tvHostComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review_vertical, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        
        holder.tvUserName.text = review.name ?: "کاربر جاباما"
        
        // Time elapsed (e.g. "اقامت ۱۴ روز پیش")
        val stayTime = review.subTitles?.firstOrNull() ?: ""
        holder.tvStayDate.text = NumberUtils.toPersianDigits(stayTime)
        
        // Stay duration (e.g. "۶ شب اقامت در اقامتگاه")
        val stayDur = review.reviewInfo?.firstOrNull()?.text ?: ""
        holder.tvStayDuration.text = NumberUtils.toPersianDigits(stayDur)
        
        holder.tvComment.text = review.comment
        
        // Stars
        val rating = (review.overalRating ?: 5).toDouble()
        for (i in 0 until holder.llStars.childCount) {
            val star = holder.llStars.getChildAt(i) as ImageView
            if (i < rating.toInt()) {
                star.setImageResource(R.drawable.ic_star)
                star.imageTintList = android.content.res.ColorStateList.valueOf(0xFFFFB400.toInt())
            } else {
                star.setImageResource(R.drawable.ic_star)
                star.imageTintList = android.content.res.ColorStateList.valueOf(0xFFDDDDDD.toInt())
            }
        }

        // Avatar
        val avatarUrl = review.image
        if (!avatarUrl.isNullOrEmpty()) {
            holder.ivUserAvatar.load(avatarUrl) {
                placeholder(R.color.gray_placeholder)
                error(R.color.gray_placeholder)
            }
        } else {
            holder.ivUserAvatar.setImageResource(R.color.gray_placeholder)
        }

        // Host Response
        if (review.response != null) {
            holder.llHostResponse.visibility = View.VISIBLE
            holder.tvHostName.text = review.response.name
            holder.tvHostComment.text = review.response.comment
            
            val hostAvatarUrl = review.response.image
            if (!hostAvatarUrl.isNullOrEmpty()) {
                holder.ivHostAvatar.load(hostAvatarUrl) {
                    placeholder(R.color.gray_placeholder)
                    error(R.color.gray_placeholder)
                }
            } else {
                holder.ivHostAvatar.setImageResource(R.color.gray_placeholder)
            }
        } else {
            holder.llHostResponse.visibility = View.GONE
        }
    }

    override fun getItemCount() = reviews.size
}
