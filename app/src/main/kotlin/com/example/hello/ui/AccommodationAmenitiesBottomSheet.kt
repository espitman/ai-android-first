package com.example.hello.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hello.R
import com.example.hello.data.models.AmenityV2
import com.example.hello.data.models.LocalizedName
import com.example.hello.ui.adapters.AmenitiesV2Adapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.hello.data.network.RetrofitClient

class AccommodationAmenitiesBottomSheet : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_amenities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnClose).setOnClickListener {
            dismiss()
        }

        val accId = arguments?.getString("ACC_ID") ?: ""
        val rv = view.findViewById<RecyclerView>(R.id.rvAmenitiesList)
        rv.layoutManager = LinearLayoutManager(requireContext())
        
        if (accId.isNotEmpty()) {
            loadAmenities(accId, rv)
        }
    }

    private fun loadAmenities(id: String, rv: RecyclerView) {
        lifecycleScope.launch {
            try {
                val url = "https://gw.jabama.com/api/v1/accommodation/public/guest/accommodations/amenities/$id?important=false"
                val response = RetrofitClient.getApiService().getAmenities(url)
                response.result?.amenities?.let { categories ->
                    rv.adapter = AmenitiesV2Adapter(categories)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        fun newInstance(accommodationId: String): AccommodationAmenitiesBottomSheet {
            return AccommodationAmenitiesBottomSheet().apply {
                arguments = Bundle().apply {
                    putString("ACC_ID", accommodationId)
                }
            }
        }
    }
}
