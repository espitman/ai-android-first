package com.example.hello.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hello.R
import com.example.hello.data.network.RetrofitClient
import com.example.hello.ui.adapters.ReviewsVerticalAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccommodationReviewsBottomSheet : BottomSheetDialogFragment() {

    private var accommodationId: String? = null

    companion object {
        fun newInstance(accommodationId: String): AccommodationReviewsBottomSheet {
            val fragment = AccommodationReviewsBottomSheet()
            val args = Bundle()
            args.putString("id", accommodationId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accommodationId = arguments?.getString("id")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_reviews, container, false)
    }

    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false
    private val pageSize = 10
    private var adapter: ReviewsVerticalAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvReviews = view.findViewById<RecyclerView>(R.id.rvReviews)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val tvEmptyState = view.findViewById<TextView>(R.id.tvEmptyState)
        val btnClose = view.findViewById<View>(R.id.btnClose)

        btnClose.setOnClickListener { dismiss() }

        val layoutManager = LinearLayoutManager(context)
        rvReviews.layoutManager = layoutManager
        
        adapter = ReviewsVerticalAdapter(mutableListOf())
        rvReviews.adapter = adapter

        // Implementation of infinite scroll
        rvReviews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                if (dy > 0) { // Scrolling down
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= pageSize) {
                            loadMoreReviews(progressBar, tvEmptyState)
                        }
                    }
                }
            }
        })

        loadMoreReviews(progressBar, tvEmptyState)
    }

    private fun loadMoreReviews(progressBar: ProgressBar, tvEmptyState: TextView) {
        val id = accommodationId ?: return
        if (isLoading) return
        
        isLoading = true
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                // Endpoint example with params: ?page=3&size=10&sortBy=LowestRate
                val url = "https://gw.jabama.com/api/v2/reviews/place/$id?page=$currentPage&size=$pageSize"
                
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.getApiService().getReviews(url)
                }

                if (response.success && response.result != null) {
                    val reviews = response.result.reviews ?: emptyList()
                    if (reviews.isNotEmpty()) {
                        adapter?.addReviews(reviews)
                        tvEmptyState.visibility = View.GONE
                        currentPage++
                        
                        // If we got fewer items than requested, it's likely the last page
                        if (reviews.size < pageSize) {
                            isLastPage = true
                        }
                    } else {
                        if (currentPage == 1) {
                            tvEmptyState.visibility = View.VISIBLE
                        }
                        isLastPage = true
                    }
                } else {
                    if (currentPage == 1) tvEmptyState.visibility = View.VISIBLE
                    isLastPage = true
                }
            } catch (e: Exception) {
                if (currentPage == 1) {
                    Toast.makeText(context, "خطا در دریافت نظرات: ${e.message}", Toast.LENGTH_SHORT).show()
                    tvEmptyState.visibility = View.VISIBLE
                }
            } finally {
                isLoading = false
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}
