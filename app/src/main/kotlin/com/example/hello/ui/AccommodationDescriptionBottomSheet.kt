package com.example.hello.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hello.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AccommodationDescriptionBottomSheet : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnClose).setOnClickListener {
            dismiss()
        }

        val code = arguments?.getInt("CODE") ?: 0
        val title = arguments?.getString("TITLE") ?: "توضیحات اقامتگاه"
        val extraList = arguments?.getParcelableArrayList<com.example.hello.data.models.ExtraDescriptionItem>("EXTRA_LIST")

        val sheetTitle = view.findViewById<TextView>(R.id.tvFullDescTitle)
        sheetTitle.text = "$title (کد اقامتگاه: $code)"
        val vazirFont = androidx.core.content.res.ResourcesCompat.getFont(requireContext(), R.font.vazirmatn)
        sheetTitle.typeface = android.graphics.Typeface.create(vazirFont, android.graphics.Typeface.BOLD)
        
        val container = view.findViewById<android.widget.LinearLayout>(R.id.llExtraDesc).parent as android.widget.LinearLayout
        
        view.findViewById<TextView>(R.id.tvFullDescBody).visibility = View.GONE
        view.findViewById<View>(R.id.llExtraDesc).visibility = View.GONE
        view.findViewById<View>(R.id.llSpaceDesc).visibility = View.GONE
        view.findViewById<View>(R.id.llCommonFacilities).visibility = View.GONE
        view.findViewById<View>(R.id.llNotes).visibility = View.GONE

        extraList?.forEachIndexed { index, item ->
            if (!item.text.isNullOrEmpty()) {
                if (index > 0) {
                    val divider = View(requireContext()).apply {
                        layoutParams = android.widget.LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                        ).apply { setMargins(0, 48, 0, 24) }
                        setBackgroundColor(android.graphics.Color.parseColor("#F0F0F0"))
                    }
                    container.addView(divider)
                }

                val titleTv = TextView(requireContext()).apply {
                    text = item.title
                    setTextColor(android.graphics.Color.parseColor("#1d2830"))
                    textSize = 17f
                    typeface = android.graphics.Typeface.create(vazirFont, android.graphics.Typeface.BOLD)
                }
                container.addView(titleTv)

                val bodyTv = TextView(requireContext()).apply {
                    text = item.text
                    setTextColor(android.graphics.Color.parseColor("#444444"))
                    textSize = 15f
                    setLineSpacing(12f, 1f)
                    val lp = android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    lp.topMargin = 24
                    layoutParams = lp
                    typeface = vazirFont
                }
                container.addView(bodyTv)
            }
        }
    }

    companion object {
        fun newInstance(
            code: Int,
            title: String,
            extraList: List<com.example.hello.data.models.ExtraDescriptionItem>?
        ): AccommodationDescriptionBottomSheet {
            return AccommodationDescriptionBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt("CODE", code)
                    putString("TITLE", title)
                    putParcelableArrayList("EXTRA_LIST", extraList?.let { ArrayList(it) })
                }
            }
        }
    }
}
