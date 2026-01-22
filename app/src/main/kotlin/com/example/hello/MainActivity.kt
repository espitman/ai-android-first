package com.example.hello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.hello.data.network.RetrofitClient
import com.example.hello.data.network.ServerConfig
import com.example.hello.ui.SettingsActivity
import com.example.hello.ui.adapters.MainAdapter

class MainActivity : AppCompatActivity() {
    
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        
        ServerConfig.init(this)
        
        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout)
        recyclerView = findViewById(R.id.rvMain)
        progressBar = findViewById(R.id.progressBar)
        
        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        
        // Setup Navigation
        setupNavigation()
        
        // Load data
        loadHomeData()
    }
    
    private fun setupNavigation() {
        val navView = findViewById<NavigationView>(R.id.navView)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
        
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
            }
            drawerLayout.closeDrawer(GravityCompat.END)
            true
        }
    }
    
    private fun loadHomeData() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
        
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.getApiService().getPages()
                }
                
                val widgets = response.widgets.sortedBy { it.order }
                recyclerView.adapter = MainAdapter(widgets)
                
                // Simple approach: just show it
                recyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                Log.e("MainActivity", "Error loading data", e)
                Toast.makeText(this@MainActivity, "خطا در بارگذاری: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }
}
