package com.example.hello.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.hello.R
import com.example.hello.data.network.RetrofitClient
import com.example.hello.ui.adapters.ImageAlbumAdapter
import com.example.hello.ui.adapters.DetailsBadgeAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.launch

class AccommodationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accommodation_detail)
        supportActionBar?.hide()

        val accId = intent.getStringExtra("ACCOMMODATION_CODE") ?: ""
        val initialTitle = intent.getStringExtra("ACCOMMODATION_TITLE") ?: ""
        
        Log.d("AccDetail", "Loading detail for ID: $accId")
        
        findViewById<TextView>(R.id.tvDetailTitle).text = initialTitle
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { onBackPressed() }

        if (accId.isNotEmpty()) {
            val skeleton = findViewById<View>(R.id.skeletonLayout)
            val pulseAnim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.skeleton_pulse)
            skeleton.startAnimation(pulseAnim)
            
            loadAccommodationDetails(accId)
        }
    }

    private fun loadAccommodationDetails(id: String) {
        lifecycleScope.launch {
            try {
                // Using the specific URL requested by user
                val url = "https://gw.jabama.com/api/v1/accommodations/$id?reversePeriods=true&withPanoramic=true"
                Log.d("AccDetail", "Calling URL: $url")
                val response = RetrofitClient.getApiService().getAccommodationDetails(url)
                
                if (response.success) {
                    Log.d("AccDetail", "API Success: ${response.result.item.title}")
                    updateUI(response.result)
                } else {
                    Log.e("AccDetail", "API Success False")
                    val skeleton = findViewById<View>(R.id.skeletonLayout)
                    skeleton.clearAnimation()
                    skeleton.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e("AccDetail", "API Error: ${e.message}")
                e.printStackTrace()
                val skeleton = findViewById<View>(R.id.skeletonLayout)
                skeleton.clearAnimation()
                skeleton.visibility = View.GONE
            }
        }
    }

    private fun updateUI(result: com.example.hello.data.models.AccommodationDetailResult) {
        val item = result.item
        val meta = result.meta
        
        val skeleton = findViewById<View>(R.id.skeletonLayout)
        skeleton.clearAnimation()
        skeleton.visibility = View.GONE
        findViewById<View>(R.id.contentLayout).visibility = View.VISIBLE
        
        findViewById<TextView>(R.id.tvDetailTitle).text = item.title
        findViewById<TextView>(R.id.tvAccommodationCode).text = "کد: ${item.code}"

        // Rating & Reviews
        item.rateAndReview?.let {
            findViewById<TextView>(R.id.tvRatingScore).text = formatNumber(it.score)
            findViewById<TextView>(R.id.tvReviewCount).text = "(${formatNumber(it.count)} نظر ثبت شده)"
        }

        // Location
        item.placeOfResidence?.area?.city?.let { city ->
            val cityName = city.name?.fa ?: ""
            val provinceName = city.province?.name?.fa ?: ""
            findViewById<TextView>(R.id.tvLocation).text = "استان $provinceName، $cityName"
        }

        // Discount
        val discount = item.maxDiscountPercent ?: 0
        val tvDiscount = findViewById<TextView>(R.id.tvDiscountBadge)
        if (discount > 0) {
            tvDiscount.visibility = View.VISIBLE
            tvDiscount.text = "٪ تا ${formatNumber(discount)} درصد تخفیف"
        } else {
            tvDiscount.visibility = View.GONE
        }

        // Host Section
        item.typeDetails?.let {
            findViewById<TextView>(R.id.tvAccTypeFa).text = it.title_fa
        }
        
        var hostName = ""
        var hostAvatar = ""

        // Priority 1: Meta HostInfo (Highest accuracy)
        meta?.hostInfo?.let { hi ->
            hostName = if (!hi.fullName.isNullOrEmpty()) hi.fullName 
                       else "${hi.firstName ?: ""} ${hi.lastName ?: ""}".trim()
            hostAvatar = hi.avatar ?: ""
        }

        // Priority 2: Item OwnerName
        if (hostName.isEmpty()) hostName = item.ownerName ?: ""

        // Priority 3: Dynamic host element check (fallback)
        if (hostName.isEmpty() || hostAvatar.isEmpty()) {
            item.host?.let { hostElement ->
                if (hostElement.isJsonObject) {
                    val hostObj = hostElement.asJsonObject
                    if (hostName.isEmpty() && hostObj.has("fullName")) {
                        hostName = hostObj.get("fullName").asString
                    }
                    if (hostAvatar.isEmpty() && hostObj.has("avatar")) {
                        hostAvatar = hostObj.get("avatar").asString
                    }
                }
            }
        }

        if (hostName.isEmpty()) hostName = "میزبان جاباما"
        
        val cityName = item.placeOfResidence?.area?.city?.name?.fa ?: ""
        val typeName = item.typeDetails?.title_fa ?: "اقامتگاه"
        findViewById<TextView>(R.id.tvHostSubtitle).text = "اجاره $typeName در $cityName به میزبانی $hostName"
        
        val ivAvatar = findViewById<ImageView>(R.id.ivHostAvatar)
        if (hostAvatar.isNotEmpty()) {
            ivAvatar.load(hostAvatar) {
                crossfade(true)
                placeholder(R.color.gray_placeholder)
                error(R.color.gray_placeholder)
            }
        } else {
            ivAvatar.setImageResource(R.color.gray_placeholder)
        }

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
        
        // Main Badges
        item.badges?.main?.let { mainBadges ->
            val llMain = findViewById<View>(R.id.llMainBadges)
            val rvMain = findViewById<RecyclerView>(R.id.rvBadgesMain)
            val tvTitle = findViewById<TextView>(R.id.tvMainBadgesTitle)
            
            val sectionTypeName = item.typeDetails?.title_fa ?: ""
            if (sectionTypeName.isNotEmpty()) {
                tvTitle.text = "مشخصات کلی $sectionTypeName"
            }
            
            rvMain.layoutManager = LinearLayoutManager(this)
            rvMain.adapter = DetailsBadgeAdapter(mainBadges)
            llMain.visibility = if (mainBadges.isNotEmpty()) View.VISIBLE else View.GONE
        } ?: run {
            findViewById<View>(R.id.llMainBadges).visibility = View.GONE
        }

        // Secondary Badges
        item.badges?.secondary?.let { secondaryBadges ->
            val rvBadges = findViewById<RecyclerView>(R.id.rvBadgesSecondary)
            rvBadges.layoutManager = LinearLayoutManager(this)
            rvBadges.adapter = DetailsBadgeAdapter(secondaryBadges)
            rvBadges.visibility = if (secondaryBadges.isNotEmpty()) View.VISIBLE else View.GONE
        } ?: run {
            findViewById<View>(R.id.rvBadgesSecondary).visibility = View.GONE
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

    private fun formatNumber(number: Double): String {
        return java.text.NumberFormat.getInstance(java.util.Locale("fa", "IR")).format(number)
    }
}
