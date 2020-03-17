package com.example.demoapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.demoapp.R
import com.example.demoapp.ui.dashboard.DashboardActivity

class SplashActivity : AppCompatActivity() {
    val SPLASH_TIMEOUT: Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkTokenAndNavigate()
    }

    private fun checkTokenAndNavigate() {
        Handler().postDelayed(Runnable {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }, SPLASH_TIMEOUT)
    }
}
