package com.example.hello.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.hello.R
import com.example.hello.data.network.RetrofitClient
import com.example.hello.ui.adapters.ImageAlbumAdapter
import kotlinx.coroutines.launch

class AccommodationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accommodation_detail)
        supportActionBar?.hide()

        val accId = intent.getStringExtra("ACCOMMODATION_CODE") ?: ""
        val initialTitle = intent.getStringExtra("ACCOMMODATION_TITLE") ?: ""
        
        findViewById<TextView>(R.id.tvDetailTitle).text = initialTitle
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { onBackPressed() }

        if (accId.isNotEmpty()) {
            loadAccommodationDetails(accId)
        }
    }

    private fun loadAccommodationDetails(id: String) {
        lifecycleScope.launch {
            try {
                // Using the specific URL requested by user
                val url = "https://gw.jabama.com/api/v1/accommodations/$id?reversePeriods=true&withPanoramic=true"
                val response = RetrofitClient.getApiService().getAccommodationDetails(url)
                
                if (response.success) {
                    val item = response.result.item
                    updateUI(item)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateUI(item: com.example.hello.data.models.AccommodationDetailItem) {
        findViewById<TextView>(R.id.tvDetailTitle).text = item.title
        findViewById<TextView>(R.id.tvAccommodationCode).text = "کد اقامتگاه: ${item.code}"

        // Image Album sorted by imagesSort
        val sortedImages = item.placeImages
            .sortedBy { image -> 
                val index = item.imagesSort.indexOf(image.uploadId)
                if (index != -1) index else Int.MAX_VALUE
            }
            .map { it.url }

        val viewPager = findViewById<ViewPager2>(R.id.vpImageAlbum)
        val tvCount = findViewById<TextView>(R.id.tvImageCount)

        viewPager.adapter = ImageAlbumAdapter(sortedImages) { position ->
            val intent = android.content.Intent(this, ImageLightboxActivity::class.java).apply {
                putStringArrayListExtra("IMAGES", ArrayList(sortedImages))
                putExtra("INDEX", position)
            }
            startActivity(intent)
        }
        
        // Setup indicator
        if (sortedImages.isNotEmpty()) {
            tvCount.visibility = View.VISIBLE
            setCounterText(tvCount, 0, sortedImages.size)
            
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    setCounterText(tvCount, position, sortedImages.size)
                }
            })
        } else {
            tvCount.visibility = View.GONE
        }
    }

    private fun setCounterText(textView: TextView, position: Int, total: Int) {
        val fPos = formatNumber(position + 1)
        val fTotal = formatNumber(total)
        textView.text = "$fPos از $fTotal"
    }

    private fun formatNumber(number: Int): String {
        return java.text.NumberFormat.getInstance(java.util.Locale("fa", "IR")).format(number)
    }
}
