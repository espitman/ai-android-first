package com.example.hello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(com.example.hello.R.layout.activity_main)
        supportActionBar?.hide()
        
        // Setup Slider
        val viewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(com.example.hello.R.id.viewPagerSlider)
        
        lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val response = com.example.hello.data.network.RetrofitClient.apiService.getPages()
                android.util.Log.d("API_DEBUG", "Response received: ${response}")
                val sliderWidget = response.widgets.find { it.type == "slider" }
                val sliderItems = sliderWidget?.slider?.items ?: emptyList()
                
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    if (sliderItems.isNotEmpty()) {
                        android.util.Log.d("API_DEBUG", "Slider items found: ${sliderItems.size}")
                        viewPager.adapter = com.example.hello.ui.adapters.SliderAdapter(sliderItems)
                    } else {
                         android.util.Log.e("API_DEBUG", "No slider items found. Widgets: ${response.widgets.map { it.type }}")
                         android.widget.Toast.makeText(this@MainActivity, "Api Connected but No Slider", android.widget.Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    android.util.Log.e("API_DEBUG", "Error fetching data: ${e.message}", e)
                    android.widget.Toast.makeText(this@MainActivity, "Error: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
