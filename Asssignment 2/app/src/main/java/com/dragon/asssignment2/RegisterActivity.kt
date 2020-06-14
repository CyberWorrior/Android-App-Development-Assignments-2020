package com.dragon.asssignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    lateinit var name:EditText
    lateinit var email:EditText
    lateinit var mobile:EditText
    lateinit var address:EditText
    lateinit var password:EditText
    lateinit var cpassword:EditText
    lateinit var registerbtn:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title = "Register"

        name = findViewById(R.id.txtRegisterName)
        email= findViewById(R.id.txtRegisterEmail)
        mobile = findViewById(R.id.txtRegisterMobile)
        address = findViewById(R.id.txtRegisterAddress)
        password = findViewById(R.id.txtRegisterPassword)
        cpassword = findViewById(R.id.txtRegisterCPassword)
        registerbtn = findViewById(R.id.btnRegister)

        registerbtn.setOnClickListener{
            var intent = Intent(this@RegisterActivity,DisplayRegister::class.java)
            var StringName = name.text.toString()
            var StringEmail = email.text.toString()
            var StringMobile = mobile.text.toString()
            var StringAddress = address.text.toString()
            var StringPassword = password.text.toString()
            var StringCPassword = cpassword.text.toString()

            if(StringName!="" && StringEmail!="" && StringMobile!="" && StringAddress!="" && StringPassword!="" && StringCPassword!=""){
                if(StringPassword==StringCPassword){
                    intent.putExtra("name",StringName)
                    intent.putExtra("email",StringEmail)
                    intent.putExtra("mobile",StringMobile)
                    intent.putExtra("address",StringAddress)
                    intent.putExtra("password",StringPassword)
                    startActivity(intent)
                }else{
                    Toast.makeText(this@RegisterActivity,"*Passwords Do Not Match*",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@RegisterActivity,"Please Enter All Your Details",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
