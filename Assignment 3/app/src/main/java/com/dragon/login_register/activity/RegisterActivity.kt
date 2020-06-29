package com.dragon.login_register.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import com.dragon.login_register.R

class RegisterActivity : AppCompatActivity() {

    lateinit var etFirstName:EditText
    lateinit var etLastName:EditText
    lateinit var etEmail:EditText
    lateinit var etPassword:EditText
    lateinit var etRePassword:EditText
    lateinit var btnRegButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etFirstName = findViewById(R.id.etRegFirstName)
        etLastName = findViewById(R.id.etRegLastName)
        etEmail =findViewById(R.id.etRegEmail)
        etPassword = findViewById(R.id.etRegPassword)
        etRePassword =findViewById(R.id.etRegRePassword)
        btnRegButton =findViewById(R.id.btnRegButton)
        
        btnRegButton.setOnClickListener{
            var StringFirstName = etFirstName.text.toString()
            var StringLastName = etLastName.text.toString()
            var StringEmail = etEmail.text.toString()
            var StringPassword = etPassword.text.toString()
            var StringRePassword = etRePassword.text.toString()
            if(validateEmailAddress(etEmail) && StringPassword == StringRePassword){
            }
        }
    }
    private fun validateEmailAddress (email : EditText):Boolean{
        var StringEmail = email.text.toString()

        return !StringEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(StringEmail).matches()
    }
}
