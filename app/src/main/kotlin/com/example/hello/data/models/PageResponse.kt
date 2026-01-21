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
    val categories: CategoriesData? = null
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
