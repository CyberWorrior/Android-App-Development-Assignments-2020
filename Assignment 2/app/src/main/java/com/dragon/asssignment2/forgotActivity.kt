package com.dragon.asssignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class forgotActivity : AppCompatActivity() {

    lateinit var mobile:EditText
    lateinit var email:EditText
    lateinit var nextForgot :Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)


        nextForgot = findViewById(R.id.btnForgotNext)
        mobile = findViewById(R.id.etMobile)
        email = findViewById(R.id.etEmail)

        nextForgot.setOnClickListener{
            var intent = Intent(this@forgotActivity, DisplayActivity::class.java)
            var StringEmail = email.text.toString()
            var StringMobile = mobile.text.toString()

            if(StringEmail!="" && StringMobile!=""){
                intent.putExtra("username",StringEmail)
                intent.putExtra("password",StringMobile)
                startActivity(intent)
            }
        }
        
    }
}
