package com.example.demoapp.ui.webview

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.demoapp.R
import com.example.demoapp.components.Initializer
import com.example.demoapp.ui.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity : AppCompatActivity(), Initializer {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        initializeUI()
    }

    class MyWebChromeClient internal constructor(
        val mProgressBar: ProgressBar
    ) :
        WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.visibility = View.GONE
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun initializeUI() {

        val actionbar = supportActionBar
        actionbar?.title = intent.getStringExtra("title")
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        webView.webChromeClient = MyWebChromeClient(progressBar)
        var url = intent.getStringExtra("url");
        webView.loadUrl(url)
    }

    override fun initializeViewModel() {}

    override fun subscribeAttributes() {}
}
