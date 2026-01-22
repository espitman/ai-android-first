package com.example.hello.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.hello.R
import com.example.hello.ui.adapters.LightboxAdapter
import java.text.NumberFormat
import java.util.Locale

class ImageLightboxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_lightbox)
        supportActionBar?.hide()

        val images = intent.getStringArrayListExtra("IMAGES") ?: arrayListOf()
        val startIndex = intent.getIntExtra("INDEX", 0)

        val viewPager = findViewById<ViewPager2>(R.id.vpLightbox)
        val tvCount = findViewById<TextView>(R.id.tvCount)
        val btnClose = findViewById<ImageButton>(R.id.btnClose)

        viewPager.adapter = LightboxAdapter(images)
        viewPager.setCurrentItem(startIndex, false)

        btnClose.setOnClickListener { finish() }

        updateCount(tvCount, startIndex, images.size)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateCount(tvCount, position, images.size)
            }
        })
    }

    private fun updateCount(textView: TextView, position: Int, total: Int) {
        val fPos = NumberFormat.getInstance(Locale("fa", "IR")).format(position + 1)
        val fTotal = NumberFormat.getInstance(Locale("fa", "IR")).format(total)
        textView.text = "$fPos از $fTotal"
    }
}
