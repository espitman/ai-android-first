package com.example.hello.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hello.R
import com.example.hello.data.models.CancellationPolicyV2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CancellationPolicyBottomSheet : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private var policyV2: CancellationPolicyV2? = null
    private var policyText: String? = null

    companion object {
        fun newInstance(policy: CancellationPolicyV2?, text: String?): CancellationPolicyBottomSheet {
            val fragment = CancellationPolicyBottomSheet()
            val args = Bundle()
            args.putParcelable("POLICY_V2", policy)
            args.putString("POLICY_TEXT", text)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        policyV2 = arguments?.getParcelable("POLICY_V2")
        policyText = arguments?.getString("POLICY_TEXT")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_cancellation_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnClose).setOnClickListener { dismiss() }

        val tvPolicyDetails = view.findViewById<TextView>(R.id.tvPolicyDetails)
        val tvPolicyTypeTitle = view.findViewById<TextView>(R.id.idPolicyTypeTitle) // Note: used idPolicyTypeTitle from last edit
        val container = view.findViewById<LinearLayout>(R.id.llStepsContainer)

        policyV2?.let { policy ->
            tvPolicyTypeTitle.text = policy.title ?: "قوانین لغو رزرو"
            
            // Use policyText if provided, otherwise use description from V2
            tvPolicyDetails.text = policyText ?: policy.description ?: ""
            
            // Step 1: Before Check-In
            policy.beforeCheckIn?.let { step ->
                addStep(container, layoutInflater, 
                    step.title ?: "", 
                    step.text ?: "", 
                    step.color ?: "#46B89C", 
                    hasNext = true)
            }

            // Step 2: Until Check-In
            policy.untilCheckIn?.let { step ->
                addStep(container, layoutInflater, 
                    step.title ?: "", 
                    step.text ?: "", 
                    step.color ?: "#FFB400", 
                    hasNext = true)
            }

            // Step 3: After Check-In
            policy.afterCheckIn?.let { step ->
                addStep(container, layoutInflater, 
                    step.title ?: "", 
                    step.text ?: "", 
                    step.color ?: "#FF5A5F", 
                    hasNext = false)
            }
            
            // Fallback for description if it's empty but we have "متعادل" style logic
            if (tvPolicyDetails.text.isNullOrEmpty() && (policy.title?.contains("متعادل") == true)) {
                tvPolicyDetails.text = "از لحظه رزرو تا ۴ روز قبل از تاریخ ورود ۱۰٪ مبلغ شب اول و ۱۰٪ مبلغ شب‌های باقیمانده کسر می‌گردد."
            }
        }
    }

    private fun addStep(
        container: LinearLayout,
        inflater: LayoutInflater,
        title: String,
        desc: String,
        color: String,
        hasNext: Boolean = false
    ) {
        val view = inflater.inflate(R.layout.item_cancellation_step, container, false)
        view.findViewById<TextView>(R.id.tvStepTitle).text = title
        view.findViewById<TextView>(R.id.tvStepDescription).text = desc
        
        val outerCircle = view.findViewById<View>(R.id.viewCircleOuter)
        val innerCircle = view.findViewById<View>(R.id.viewCircleInner)
        val topLine = view.findViewById<View>(R.id.viewTopLine)
        val bottomLine = view.findViewById<View>(R.id.viewBottomLine)

        val tintColor = try {
            android.graphics.Color.parseColor(color)
        } catch (e: Exception) {
            android.graphics.Color.GRAY
        }
        
        outerCircle.background.setTint(tintColor)
        innerCircle.background.setTint(tintColor)
        
        topLine.visibility = View.INVISIBLE
        if (hasNext) {
            bottomLine.visibility = View.VISIBLE
            bottomLine.background.setTint(tintColor)
        } else {
            bottomLine.visibility = View.INVISIBLE
        }

        container.addView(view)
    }
}
