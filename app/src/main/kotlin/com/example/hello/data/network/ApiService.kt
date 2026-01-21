package com.example.hello.data.network

import com.example.hello.data.models.PageResponse
import retrofit2.http.GET

interface ApiService {
    @GET("api/pages")
    suspend fun getPages(): PageResponse
}
