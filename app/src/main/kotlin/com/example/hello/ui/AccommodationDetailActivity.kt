package com.example.hello.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.hello.R

class AccommodationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accommodation_detail)
        supportActionBar?.hide()

        val code = intent.getStringExtra("ACCOMMODATION_CODE") ?: "Unknown"
        val title = intent.getStringExtra("ACCOMMODATION_TITLE") ?: ""
        val imageUrl = intent.getStringExtra("ACCOMMODATION_IMAGE") ?: ""

        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val ivImage = findViewById<ImageView>(R.id.ivDetailImage)
        val tvCode = findViewById<TextView>(R.id.tvAccommodationCode)

        tvTitle.text = title
        tvCode.text = "کد اقامتگاه: $code"
        
        ivImage.load(imageUrl) {
            crossfade(true)
            placeholder(R.color.gray_placeholder)
            error(R.color.gray_placeholder)
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }
    }
}
