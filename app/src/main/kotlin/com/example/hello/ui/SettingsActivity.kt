package com.example.hello.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hello.R
import com.example.hello.data.network.ServerConfig

class SettingsActivity : AppCompatActivity() {

    private lateinit var rgServerUrl: RadioGroup
    private lateinit var rbLocal: RadioButton
    private lateinit var rbVercel: RadioButton
    private lateinit var tvCurrentUrl: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        rgServerUrl = findViewById(R.id.rgServerUrl)
        rbLocal = findViewById(R.id.rbLocal)
        rbVercel = findViewById(R.id.rbVercel)
        tvCurrentUrl = findViewById(R.id.tvCurrentUrl)

        // Back button
        btnBack.setOnClickListener {
            finish()
        }

        // Set current selection
        if (ServerConfig.isLocalSelected()) {
            rbLocal.isChecked = true
        } else {
            rbVercel.isChecked = true
        }
        updateUrlDisplay()

        // Handle selection change
        rgServerUrl.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLocal -> ServerConfig.setServerUrl(ServerConfig.URL_LOCAL)
                R.id.rbVercel -> ServerConfig.setServerUrl(ServerConfig.URL_VERCEL)
            }
            updateUrlDisplay()
        }
    }

    private fun updateUrlDisplay() {
        tvCurrentUrl.text = "آدرس فعلی:\n${ServerConfig.getServerUrl()}"
    }
}
