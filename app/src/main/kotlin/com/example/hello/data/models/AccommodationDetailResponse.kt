package com.example.hello.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

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
    val badges: AccommodationBadges? = null,
    val spaceDescription: String? = null,
    val commonFacilitiesDescription: String? = null,
    val notesDescription: String? = null,
    val extraDescription: List<ExtraDescriptionItem>? = null,
    val amenities: List<Amenity>? = null,
    val missedAmenities: List<Amenity>? = null,
    val amenitiesV2: List<AmenityV2>? = null,
    val nearbyCentersV2: List<NearbyCenterCategory>? = null,
    val restrictedRules: List<AccommodationRule>? = null,
    val negativeRestrictedRules: List<AccommodationRule>? = null,
    val cancellationPolicy: CancellationPolicy? = null,
    val cancellationPolicyV2: CancellationPolicy? = null,
    val cancellationPolicyDetails: CancellationPolicyV2? = null,
    val cancellationPolicyText: String? = null
)

@Parcelize
data class CancellationPolicyV2(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val beforeCheckIn: CancellationStepDetail? = null,
    val untilCheckIn: CancellationStepDetail? = null,
    val afterCheckIn: CancellationStepDetail? = null
) : Parcelable

@Parcelize
data class CancellationStepDetail(
    val title: String? = null,
    val text: String? = null,
    val color: String? = null
) : Parcelable

@Parcelize
data class CancellationPolicy(
    val id: String? = null,
    val title: String? = null,
    val beforeCheckIn: PolicyTimeFrame? = null,
    val untilCheckIn: PolicyTimeFrame? = null,
    val afterCheckIn: PolicyTimeFrame? = null
) : Parcelable

@Parcelize
data class PolicyTimeFrame(
    val days: Int? = null,
    val firstNightPercent: Int? = null,
    val remainingNightsPercent: Int? = null,
    val passedNightsPercent: Int? = null
) : Parcelable

@Parcelize
data class AccommodationRule(
    val id: String? = null,
    val name: String? = null,
    val positive: String? = null,
    val negative: String? = null
) : Parcelable

@Parcelize
data class ExtraDescriptionItem(
    val title: String? = null,
    val subTitle: String? = null,
    val text: String? = null
) : Parcelable

data class AccommodationBadges(
    val main: List<BadgeDetail>? = null,
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
    val area: ResidenceArea? = null,
    val location: ResidentLocation? = null
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

@Parcelize
data class LocalizedName(
    val fa: String? = null,
    val en: String? = null
) : Parcelable

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
    val iranianToiletsCount: Int? = null,
    val bedroomsCount: Int? = null,
    val toiletsCount: Int? = null,
    val floor: Int? = null
)

@Parcelize
data class Amenity(
    val id: String? = null,
    val icon: AmenityIcon? = null,
    val title: LocalizedName? = null,
    val subTitle: String? = null
) : Parcelable

@Parcelize
data class AmenityIcon(
    val url: String? = null
) : Parcelable

@Parcelize
data class AmenityV2(
    val icon: AmenityIcon? = null,
    val state: Boolean? = null,
    val title: LocalizedName? = null,
    @SerializedName("items") val subItems: List<String>? = null
) : Parcelable

data class AmenitiesResponse(
    val result: AmenitiesResult? = null
)

data class AmenitiesResult(
    val amenities: List<AmenityCategory>? = null
)

@Parcelize
data class AmenityCategory(
    val title: LocalizedName? = null,
    val items: List<AmenityV2>? = null
) : Parcelable

@Parcelize
data class ResidentLocation(
    val lat: Double,
    val lng: Double,
    val radius: Int? = null
) : Parcelable

@Parcelize
data class NearbyCenterCategory(
    val title: String? = null,
    val items: List<NearbyCenterItem>? = null
) : Parcelable

@Parcelize
data class NearbyCenterItem(
    val key: String? = null,
    val value: String? = null,
    val accessibleBy: String? = null
) : Parcelable
