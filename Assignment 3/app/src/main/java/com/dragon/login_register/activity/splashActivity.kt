package com.dragon.login_register.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dragon.login_register.R

class splashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val splashAct = Intent(this@splashActivity,
                LoginIn::class.java)
            startActivity(splashAct)
        },2000)
    }

    override fun onPause() {
        finish()
        super.onPause()
    }
}
