package com.example.hello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val myWebView: android.webkit.WebView = findViewById(R.id.webview)
        myWebView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
        }
        myWebView.webViewClient = object : android.webkit.WebViewClient() {
            override fun onReceivedError(view: android.webkit.WebView?, request: android.webkit.WebResourceRequest?, error: android.webkit.WebResourceError?) {
                super.onReceivedError(view, request, error)
                android.util.Log.e("WebView", "Error: ${error?.description}")
            }
        }
        android.webkit.CookieManager.getInstance().setAcceptCookie(true)
        myWebView.loadUrl("https://www.jabama.com")
    }
}
