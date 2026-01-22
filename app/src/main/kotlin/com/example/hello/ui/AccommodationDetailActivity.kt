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

class AccommodationDetailActivity : AppCompatActivity(), com.google.android.gms.maps.OnMapReadyCallback {

    private var googleMap: com.google.android.gms.maps.GoogleMap? = null
    private var currentItem: com.example.hello.data.models.AccommodationDetailItem? = null

    override fun onMapReady(map: com.google.android.gms.maps.GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isMapToolbarEnabled = false
        currentItem?.placeOfResidence?.location?.let { loc ->
            val pos = com.google.android.gms.maps.model.LatLng(loc.lat, loc.lng)
            googleMap?.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(pos, 15f))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accommodation_detail)
        supportActionBar?.hide()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as? com.google.android.gms.maps.SupportMapFragment
        mapFragment?.getMapAsync(this)

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
                
                if (response.success && response.result != null) {
                    currentItem = response.result.item
                    Log.d("AccDetail", "API Success: ${response.result.item.title}")
                    updateUI(response.result)
                } else {
                    Log.e("AccDetail", "API Success False or Result Null")
                    handleError()
                }
            } catch (e: Exception) {
                Log.e("AccDetail", "Load Error: ${e.message}")
                e.printStackTrace()
                handleError()
            }
        }
    }

    private fun handleError() {
        val skeleton = findViewById<View>(R.id.skeletonLayout)
        skeleton.clearAnimation()
        skeleton.visibility = View.GONE
        findViewById<View>(R.id.contentLayout).visibility = View.VISIBLE
        // Optionally show an error message
    }

    private fun updateUI(result: com.example.hello.data.models.AccommodationDetailResult) {
        val item = result.item
        val meta = result.meta
        
        val skeleton = findViewById<View>(R.id.skeletonLayout)
        skeleton.clearAnimation()
        skeleton.visibility = View.GONE
        findViewById<View>(R.id.contentLayout).visibility = View.VISIBLE
        
        findViewById<TextView>(R.id.tvDetailTitle).text = item.title
        findViewById<TextView>(R.id.tvAccommodationCode).text = 
            com.example.hello.utils.NumberUtils.toPersianDigits("کد: ${item.code}")

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

        // Description Section
        val typeNameTitle = item.typeDetails?.title_fa ?: "اقامتگاه"
        findViewById<TextView>(R.id.tvDescriptionTitle).text = "توضیحات $typeNameTitle"
        
        val descMain = item.description ?: ""
        if (descMain.isNotEmpty()) {
            findViewById<View>(R.id.llDescriptionSection).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tvDescriptionSummary).text = descMain
            
            findViewById<View>(R.id.btnShowMoreDescription).setOnClickListener {
                val sheet = AccommodationDescriptionBottomSheet.newInstance(
                    item.code,
                    "توضیحات $typeNameTitle",
                    item.extraDescription
                )
                sheet.show(supportFragmentManager, "DescriptionSheet")
            }
        } else {
            findViewById<View>(R.id.llDescriptionSection).visibility = View.GONE
        }

        // Amenities Section
        val amenities = item.amenities ?: emptyList()
        val missedAmenities = item.missedAmenities ?: emptyList()
        if (amenities.isNotEmpty() || missedAmenities.isNotEmpty()) {
            findViewById<View>(R.id.llAmenitiesSection).visibility = View.VISIBLE
            
            // Available Amenities (Show first 8)
            val rvAmenities = findViewById<RecyclerView>(R.id.rvAmenities)
            rvAmenities.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 4)
            rvAmenities.adapter = com.example.hello.ui.adapters.AmenitiesAdapter(amenities.take(8))

            // Missed Amenities
            if (missedAmenities.isNotEmpty()) {
                findViewById<View>(R.id.llMissedAmenities).visibility = View.VISIBLE
                val rvMissed = findViewById<RecyclerView>(R.id.rvMissedAmenities)
                rvMissed.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 4)
                rvMissed.adapter = com.example.hello.ui.adapters.AmenitiesAdapter(missedAmenities.take(4))
            } else {
                findViewById<View>(R.id.llMissedAmenities).visibility = View.GONE
            }

            // Show All Button
            findViewById<View>(R.id.btnShowAllAmenities).setOnClickListener {
                val sheet = AccommodationAmenitiesBottomSheet.newInstance(item.id)
                sheet.show(supportFragmentManager, "AmenitiesSheet")
            }
        } else {
            findViewById<View>(R.id.llAmenitiesSection).visibility = View.GONE
        }

        // Location Section
        val location = item.placeOfResidence?.location
        val nearbyCategories = item.nearbyCentersV2 ?: emptyList()

        if (location != null || nearbyCategories.isNotEmpty()) {
            findViewById<View>(R.id.llLocationSection).visibility = View.VISIBLE
            
            // Map logic handled by onMapReady and currentItem
            if (location != null && googleMap != null) {
                val pos = com.google.android.gms.maps.model.LatLng(location.lat, location.lng)
                googleMap?.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(pos, 15f))
            }

            // Neighborhood / Distances
            val listContainer = findViewById<android.widget.LinearLayout>(R.id.llNeighborhoodList)
            listContainer.removeAllViews()
            
            nearbyCategories.filter { !it.items.isNullOrEmpty() }.forEach { category ->
                val categoryView = layoutInflater.inflate(R.layout.item_neighborhood_category, listContainer, false)
                categoryView.findViewById<TextView>(R.id.tvCategoryTitle).text = category.title
                
                val itemsContainer = categoryView.findViewById<android.widget.LinearLayout>(R.id.llCategoryItems)
                category.items?.forEach { nb ->
                    val itemView = layoutInflater.inflate(R.layout.item_neighborhood, itemsContainer, false)
                    itemView.findViewById<TextView>(R.id.tvNeighborhoodTitle).text = 
                        com.example.hello.utils.NumberUtils.toPersianDigits(nb.key ?: "")
                    itemView.findViewById<TextView>(R.id.tvNeighborhoodDistance).text = 
                        com.example.hello.utils.NumberUtils.toPersianDigits(nb.value ?: "")
                    itemsContainer.addView(itemView)
                }
                listContainer.addView(categoryView)
            }
        } else {
            findViewById<View>(R.id.llLocationSection).visibility = View.GONE
        }

        // Rules Section
        findViewById<TextView>(R.id.tvRulesTitle).text = "قوانین و مقررات"
        
        findViewById<TextView>(R.id.tvCheckInTime).text = formatRuleTime(item.checkIn, true)
        findViewById<TextView>(R.id.tvCheckOutTime).text = formatRuleTime(item.checkOut, false)

        // Accommodation Rules (مقررات کلبه)
        val rulesContainer = findViewById<android.widget.LinearLayout>(R.id.llAccommodationRulesSection)
        val rulesList = findViewById<android.widget.LinearLayout>(R.id.llAccommodationRulesList)
        findViewById<TextView>(R.id.tvAccommodationRulesTitle).text = "مقررات $typeName"
        rulesList.removeAllViews()

        val rulesData = mutableListOf<Pair<String, Boolean>>()
        
        // Positive rules (ticks)
        item.restrictedRules?.forEach { rule ->
            rule.positive?.let { rulesData.add(it to true) }
        }

        // Negative rules (crosses)
        item.negativeRestrictedRules?.forEach { rule ->
            rule.negative?.let { rulesData.add(it to false) }
        }

        if (rulesData.isNotEmpty()) {
            rulesContainer.visibility = View.VISIBLE
            rulesData.forEach { (text, isPositive) ->
                val ruleView = layoutInflater.inflate(R.layout.item_accommodation_rule, rulesList, false)
                ruleView.findViewById<TextView>(R.id.tvRuleText).text = text
                ruleView.findViewById<ImageView>(R.id.ivRuleStatusIcon).setImageResource(
                    if (isPositive) R.drawable.ic_check_simple else R.drawable.ic_cross_simple
                )
                rulesList.addView(ruleView)
            }
        } else {
            rulesContainer.visibility = View.GONE
        }

        // Cancellation Policy
        findViewById<TextView>(R.id.tvCancellationPolicyTitle).text = 
            item.cancellationPolicyDetails?.title ?: item.cancellationPolicy?.title ?: "نامشخص"
            
        findViewById<View>(R.id.llCancellationSection).setOnClickListener {
            val sheet = CancellationPolicyBottomSheet.newInstance(item.cancellationPolicyDetails, item.cancellationPolicyText)
            sheet.show(supportFragmentManager, "CancellationSheet")
        }
    }

    private fun formatRuleTime(time: String?, isCheckIn: Boolean): String {
        if (time.isNullOrEmpty()) return if (isCheckIn) "۰۳:۰۰ (بعدازظهر)" else "۱۲:۰۰ (ظهر)"
        
        return try {
            val parts = time.split(":")
            val hour = parts[0].toInt()
            val minute = parts[1]
            
            val period = when {
                hour < 12 -> "(صبح)"
                hour == 12 -> "(ظهر)"
                else -> "(بعدازظهر)"
            }
            
            val displayHour = if (hour > 12) hour - 12 else hour
            val formattedHour = if (displayHour < 10) "۰$displayHour" else displayHour.toString()
            
            val result = "$formattedHour:$minute $period"
            com.example.hello.utils.NumberUtils.toPersianDigits(result)
        } catch (e: Exception) {
            com.example.hello.utils.NumberUtils.toPersianDigits(time)
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
