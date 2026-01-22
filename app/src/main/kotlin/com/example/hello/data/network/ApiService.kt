package com.example.hello.data.network

import com.example.hello.data.models.PageResponse
import retrofit2.http.GET

interface ApiService {
    @GET("api/pages")
    suspend fun getPages(): PageResponse

    @retrofit2.http.GET
    suspend fun getAccommodationDetails(@retrofit2.http.Url url: String): com.example.hello.data.models.AccommodationDetailResponse
}
