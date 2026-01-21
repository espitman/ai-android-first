package com.example.hello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import coil.load
import com.example.hello.ui.adapters.MainAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.hello.data.network.RetrofitClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        
        val rvMain = findViewById<RecyclerView>(R.id.rvMain)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.getPages()
                val sortedWidgets = response.widgets.sortedBy { it.order }
                
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    rvMain.adapter = MainAdapter(sortedWidgets)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Log.e("API_DEBUG", "Error fetching data: ${e.message}", e)
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
