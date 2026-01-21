package com.example.hello.data.models

data class PageResponse(
    val success: Boolean,
    val page: PageInfo,
    val widgets: List<Widget>
)

data class PageInfo(
    val id: String,
    val slug: String,
    val title: String,
    val description: String?,
    val active: Boolean
)

data class Widget(
    val id: String,
    val type: String,
    val order: Int,
    val slider: SliderData? = null,
    val categories: CategoriesData? = null,
    @com.google.gson.annotations.SerializedName("accommodationCarousel")
    val accommodationCarousel: AccommodationCarouselData? = null,
    val banner: BannerData? = null,
    val linkCarousel: LinkCarouselData? = null
)

data class LinkCarouselData(
    val title: String,
    val subTitle: String?,
    val items: List<LinkCarouselItem>,
    val colors: LinkCarouselColors?
)

data class LinkCarouselItem(
    val title: String,
    val image: String,
    val link: String?,
    val openInNewTab: Boolean
)

data class LinkCarouselColors(
    val title: String?,
    val subTitle: String?,
    val background: String?
)

data class BannerData(
    val gifUrl: String,
    val link: String?,
    val openInNewTab: Boolean,
    val colors: BannerColors?
)

data class BannerColors(
    val background: String?
)

data class AccommodationCarouselData(
    val title: String,
    val subTitle: String?,
    val items: List<AccommodationItem>
)

data class AccommodationItem(
    val id: String,
    val title: String,
    val images: List<String>,
    val city: String,
    val province: String,
    val roomsCount: Int,
    val rate: AccommodationRate?,
    val price: AccommodationPrice?,
    val accommodationType: String?
)

data class AccommodationRate(
    val score: Double,
    val count: Int
)

data class AccommodationPrice(
    val full: Long,
    val discounted: Long,
    val discountPercentage: Int,
    val type: String
)

data class SliderData(
    val items: List<SliderItem>
)

data class SliderItem(
    val title: String,
    val image: String,
    val link: String?,
    val openInNewTab: Boolean
)

data class CategoriesData(
    val items: List<CategoryItem>
)

data class CategoryItem(
    val title: String,
    val icon: String,
    val link: String?,
    val openInNewTab: Boolean,
    val badge: Badge? = null
)

data class Badge(
    val title: String,
    val bgColor: String?,
    val textColor: String?
)
