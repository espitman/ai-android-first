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
        val progressBar = findViewById<android.widget.ProgressBar>(com.example.hello.R.id.progressBar)
        
        // Initially hide content until loaded (optional, or just show loading on top)
        viewPager.visibility = android.view.View.INVISIBLE
        val rvCategories = findViewById<androidx.recyclerview.widget.RecyclerView>(com.example.hello.R.id.rvCategories)
        rvCategories.visibility = android.view.View.INVISIBLE

        lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val response = com.example.hello.data.network.RetrofitClient.apiService.getPages()
                android.util.Log.d("API_DEBUG", "Response received: ${response}")
                val sliderWidget = response.widgets.find { it.type == "slider" }
                val sliderItems = sliderWidget?.slider?.items ?: emptyList()
                
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    progressBar.visibility = android.view.View.GONE
                    
                    // Setup Slider
                    if (sliderItems.isNotEmpty()) {
                        android.util.Log.d("API_DEBUG", "Slider items found: ${sliderItems.size}")
                        viewPager.adapter = com.example.hello.ui.adapters.SliderAdapter(sliderItems)
                        viewPager.visibility = android.view.View.VISIBLE
                    } else {
                         android.util.Log.e("API_DEBUG", "No slider items found. Widgets: ${response.widgets.map { it.type }}")
                         android.widget.Toast.makeText(this@MainActivity, "Api Connected but No Slider", android.widget.Toast.LENGTH_LONG).show()
                    }

                    // Setup Categories
                    val categoriesWidget = response.widgets.find { it.type == "categories" }
                    val categoryItems = categoriesWidget?.categories?.items ?: emptyList()
                    // rvCategories is already declared above, no need to redeclare here
                    
                    if (categoryItems.isNotEmpty()) {
                         android.util.Log.d("API_DEBUG", "Categories found: ${categoryItems.size}")
                         rvCategories.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@MainActivity, 4)
                         // For RTL in Grid, sometimes layoutDirection needs to be handled or simply order of items.
                         // But GridLayoutManager handles 4 columns standard.
                         rvCategories.adapter = com.example.hello.ui.adapters.CategoriesAdapter(categoryItems)
                         rvCategories.visibility = android.view.View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    progressBar.visibility = android.view.View.GONE
                    android.util.Log.e("API_DEBUG", "Error fetching data: ${e.message}", e)
                    android.widget.Toast.makeText(this@MainActivity, "Error: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
