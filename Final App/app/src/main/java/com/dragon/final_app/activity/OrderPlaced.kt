package com.dragon.final_app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dragon.final_app.R

class OrderPlaced : AppCompatActivity() {
    lateinit var buttonOk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)
        buttonOk = findViewById(R.id.btnOrderOk)
        buttonOk.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}