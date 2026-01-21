package com.example.hello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(com.example.hello.R.layout.activity_main)

        val button = findViewById<android.widget.Button>(com.example.hello.R.id.btnAlert)
        button.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Hello from the button!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        val btnNext = findViewById<android.widget.Button>(com.example.hello.R.id.btnNextPage)
        btnNext.setOnClickListener {
            val intent = android.content.Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}
