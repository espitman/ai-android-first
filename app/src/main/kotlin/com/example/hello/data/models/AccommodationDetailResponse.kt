package com.example.hello.data.models

data class AccommodationDetailResponse(
    val success: Boolean,
    val result: AccommodationDetailResult
)

data class AccommodationDetailResult(
    val item: AccommodationDetailItem
)

data class AccommodationDetailItem(
    val id: String,
    val code: Int,
    val title: String,
    val description: String,
    val placeImages: List<AccommodationPlaceImage>,
    val imagesSort: List<String>,
    val placeOfResidence: PlaceOfResidence? = null,
    val maxDiscountPercent: Int? = null,
    val price: AccommodationPriceData? = null,
    val capacity: AccommodationCapacity? = null,
    val rateAndReview: AccommodationRateAndReview? = null
)

data class PlaceOfResidence(
    val area: ResidenceArea? = null
)

data class ResidenceArea(
    val city: ResidenceCity? = null
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
    val double: Int,
    val single: Int
)

data class AccommodationRateAndReview(
    val count: Int,
    val score: Double
)
