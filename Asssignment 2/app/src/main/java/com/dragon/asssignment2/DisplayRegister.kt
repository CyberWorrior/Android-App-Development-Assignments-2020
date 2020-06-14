package com.dragon.asssignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DisplayRegister : AppCompatActivity() {


    var StringName = "D"
    var StringEmail = "D"
    var StringMobile = "D"
    var StringAddress = "D"
    var StringPassword = "D"

    lateinit var name:TextView
    lateinit var email:TextView
    lateinit var mobile:TextView
    lateinit var address:TextView
    lateinit var password:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_register)

        name = findViewById(R.id.txtdisplayName)
        email = findViewById(R.id.txtdisplayEmail)
        mobile = findViewById(R.id.txtdisplayMobile)
        address = findViewById(R.id.txtdisplayAddress)
        password = findViewById(R.id.txtdisplayPassword)

        if(intent!=null){
            StringName = intent.getStringExtra("name")
            StringEmail = intent.getStringExtra("email")
            StringMobile = intent.getStringExtra("mobile")
            StringAddress = intent.getStringExtra("address")
            StringPassword = intent.getStringExtra("password")
        }

        name.text = StringName
        email.text = StringEmail
        mobile.text = StringMobile
        address.text = StringAddress
        password.text = StringPassword
    }
}
