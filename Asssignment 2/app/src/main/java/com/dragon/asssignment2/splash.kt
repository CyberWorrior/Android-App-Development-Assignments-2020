package com.dragon.asssignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide();
        setContentView(R.layout.splash)
        Handler().postDelayed({
            val startAct = Intent(this@splash,login_activity::class.java)
            startActivity(startAct)
        },2000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
