package com.dragon.asssignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DisplayActivity : AppCompatActivity() {

    var username :String? = "No Username"
    var password :String? = "No Password"
    lateinit var Usertxt:TextView
    lateinit var Passwordtxt:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.displaylogin)
        title = "Your Details"

        Usertxt = findViewById(R.id.txtUser)
        Passwordtxt = findViewById(R.id.txtPassword)

        if(intent != null){
            username = intent.getStringExtra("username")
            password = intent.getStringExtra("password")
        }
        Usertxt.text = username
        Passwordtxt.text = password


    }

}
