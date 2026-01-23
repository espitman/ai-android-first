package com.example.hello.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ReviewsResponse(
    val result: ReviewsResult? = null,
    val success: Boolean = false
)

data class ReviewsResult(
    val reviews: List<ReviewV2>? = null,
    val sorting: List<ReviewSortOption>? = null
)

@Parcelize
data class ReviewV2(
    val comment: String? = null,
    val image: String? = null,
    val name: String? = null,
    val subTitles: List<String>? = null,
    val overalRating: Int? = null,
    val reviewInfo: List<ReviewInfoItem>? = null,
    val response: ReviewHostResponse? = null
) : Parcelable

@Parcelize
data class ReviewHostResponse(
    val comment: String? = null,
    val image: String? = null,
    val name: String? = null,
    val subTitle: List<String>? = null
) : Parcelable

@Parcelize
data class ReviewSortOption(
    val key: String? = null,
    val value: String? = null,
    val isSelected: Boolean = false
) : Parcelable
