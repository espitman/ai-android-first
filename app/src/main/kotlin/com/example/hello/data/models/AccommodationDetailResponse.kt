package com.example.hello.data.models

import com.google.gson.annotations.SerializedName

data class AccommodationDetailResponse(
    val success: Boolean,
    val result: AccommodationDetailResult
)

data class AccommodationDetailResult(
    val item: AccommodationDetailItem,
    val meta: AccommodationDetailMeta? = null
)

data class AccommodationDetailMeta(
    val hostInfo: HostInfo? = null,
    val reviews: AccommodationReviews? = null
)

data class HostInfo(
    @SerializedName("fistName")
    val firstName: String? = null,
    val lastName: String? = null,
    val avatar: String? = null,
    val fullName: String? = null
)

data class AccommodationReviews(
    val overalRating: Double? = null,
    val reviewsCount: Int? = null,
    val rating: Double? = null
)

data class AccommodationDetailItem(
    val id: String,
    val code: Int,
    val title: String,
    val description: String,
    val checkIn: String?,
    val checkOut: String?,
    val placeImages: List<AccommodationPlaceImage>,
    val imagesSort: List<String>,
    val typeDetails: TypeDetails? = null,
    val host: com.google.gson.JsonElement? = null,
    val ownerName: String? = null,
    val hostProfile: HostProfile? = null,
    val placeOfResidence: PlaceOfResidence? = null,
    val price: AccommodationPriceData? = null,
    val capacity: AccommodationCapacity? = null,
    val rateAndReview: AccommodationRateAndReview? = null,
    val maxDiscountPercent: Int? = null,
    val accommodationMetrics: AccommodationMetrics? = null,
    val badges: AccommodationBadges? = null
)

data class AccommodationBadges(
    val primary: List<BadgeDetail>? = null,
    val secondary: List<BadgeDetail>? = null
)

data class BadgeDetail(
    val icon: String? = null,
    val title: String? = null,
    val description: String? = null,
    @SerializedName("subTitle") val subTitle: String? = null,
    @SerializedName("subtitle") val subtitle_lower: String? = null,
    @SerializedName("subText") val subText: String? = null,
    val data: List<String>? = null
)

data class TypeDetails(
    val title: String?,
    val title_fa: String?,
    val image: String?
)

data class HostProfile(
    val items: List<HostProfileItem>? = null
)

data class HostProfileItem(
    val icon: String?,
    val text: String?,
    val subText: String?
)

data class PlaceOfResidence(
    val area: ResidenceArea? = null
)

data class ResidenceArea(
    val city: ResidenceCity? = null,
    val areaTypeDetails: TypeDetails? = null
)

data class ResidenceCity(
    val name: LocalizedName? = null,
    val province: ResidenceProvince? = null
)

data class ResidenceProvince(
    val name: LocalizedName? = null
)

data class LocalizedName(
    val fa: String,
    val en: String
)

data class AccommodationPlaceImage(
    val type: String,
    val url: String,
    val caption: String?,
    val uploadId: String
)

data class AccommodationPriceData(
    val base: Long,
    val weekend: Long? = null,
    val holiday: Long? = null,
    val extraPeople: Map<String, Long>? = null
)

data class AccommodationCapacity(
    val guests: GuestCapacity? = null,
    val beds: BedCapacity? = null
)

data class GuestCapacity(
    val base: Int,
    val extra: Int
)

data class BedCapacity(
    val twin: Int? = null,
    val single: Int? = null,
    val double: Int? = null,
    val mattress: Int? = null
)

data class AccommodationRateAndReview(
    val count: Int,
    val score: Double
)

data class AccommodationMetrics(
    val areaSize: Int? = null,
    val buildingSize: Int? = null,
    val bathroomsCount: Int? = null,
    val bedroomsCount: Int? = null,
    val toiletsCount: Int? = null,
    val floor: Int? = null
)
