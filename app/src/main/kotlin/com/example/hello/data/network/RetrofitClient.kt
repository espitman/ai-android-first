package com.example.hello.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    
    private var retrofit: Retrofit? = null
    
    fun getApiService(): ApiService {
        val currentUrl = ServerConfig.getServerUrl()
        
        // Recreate Retrofit if URL changed or not initialized
        if (retrofit == null || retrofit?.baseUrl()?.toString() != currentUrl) {
            retrofit = Retrofit.Builder()
                .baseUrl(currentUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        
        return retrofit!!.create(ApiService::class.java)
    }
}
