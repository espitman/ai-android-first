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
    val type: String, // "slider", "categories", etc.
    val order: Int,
    val slider: SliderData? = null
    // Add other widget data types here as needed in future
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
