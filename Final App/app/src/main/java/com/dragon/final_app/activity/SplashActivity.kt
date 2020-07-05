package com.dragon.final_app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dragon.final_app.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val splashAct = Intent(
                this@SplashActivity,
                LoginIn::class.java
            )
            startActivity(splashAct)
        }, 2000)
    }

    override fun onPause() {
        finish()
        super.onPause()
    }
}
