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
import com.example.hello.data.models.ReviewDetail
import com.example.hello.utils.NumberUtils

class ReviewsAdapter(private val reviews: List<ReviewDetail>) :
    RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivUserAvatar: ImageView = view.findViewById(R.id.ivUserAvatar)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvStayDate: TextView = view.findViewById(R.id.tvStayDate)
        val tvStayDuration: TextView = view.findViewById(R.id.tvStayDuration)
        val tvComment: TextView = view.findViewById(R.id.tvComment)
        val llStars: LinearLayout = view.findViewById(R.id.llStars)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review_card, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        
        holder.tvUserName.text = review.user?.name ?: "کاربر جاباما"
        
        // Time elapsed (e.g. "اقامت ۱۳ روز پیش")
        val stayTime = review.subTitles?.firstOrNull() ?: ""
        holder.tvStayDate.text = NumberUtils.toPersianDigits(stayTime)
        
        // Stay duration (e.g. "۶ شب اقامت در اقامتگاه")
        val stayDur = review.reviewInfo?.firstOrNull()?.text ?: ""
        holder.tvStayDuration.text = NumberUtils.toPersianDigits(stayDur)
        
        holder.tvComment.text = review.comment
        
        // Stars calculation (using overalRating or rating)
        val rating = (review.overalRating ?: review.rating ?: 5).toDouble()
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
        val avatarUrl = review.user?.avatar
        if (!avatarUrl.isNullOrEmpty()) {
            holder.ivUserAvatar.load(avatarUrl) {
                placeholder(R.color.gray_placeholder)
                error(R.color.gray_placeholder)
            }
        } else {
            holder.ivUserAvatar.setImageResource(R.color.gray_placeholder)
        }
    }

    override fun getItemCount() = reviews.size
}
