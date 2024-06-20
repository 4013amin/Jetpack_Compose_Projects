package com.example.jetpackshop.webBroswers

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

import com.example.jetpackshop.ui.theme.JetPackShopTheme

class main_web : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {

                var url by remember {
                    mutableStateOf("https://bustan.tvu.ac.ir/Home/Index") // مقدار اولیه URL
                }
                var webView by remember {
                    mutableStateOf<WebView?>(null)
                }

                AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()
                        settings.javaScriptEnabled = true
                        loadUrl(url)
                        webView = this
                    }
                }, update = {
                    webView?.loadUrl(url)
                })
            }
        }
    }
}