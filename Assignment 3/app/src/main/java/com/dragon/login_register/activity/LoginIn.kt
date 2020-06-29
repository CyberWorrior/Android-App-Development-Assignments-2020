package com.dragon.login_register.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dragon.login_register.R


class LoginIn : AppCompatActivity() {

    lateinit var txtEmail:EditText
    lateinit var txtPassword:EditText
    lateinit var btnLogin:Button
    lateinit var btnRegister:Button
    lateinit var sharedPreferences: SharedPreferences
    var allCorrectLoginInfo:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        val getLoginStatus = sharedPreferences.getBoolean("isLoggedIn",false)
        setContentView(R.layout.activity_login_in)

        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        if(getLoginStatus){
            txtEmail.setText(sharedPreferences.getString("email","Your Email"))
            txtPassword.setText(sharedPreferences.getString("password","password"))
            btnLogin.setOnClickListener{

                var intent = Intent(this@LoginIn,MainActivity::class.java)

                val StringEmail = txtEmail.text.toString()
                val StringPassword = txtPassword.text.toString()

                if(validateEmailAddress(txtEmail) && txtPassword.toString().length>=8){
                    saveCredentials(StringEmail , StringPassword)
                    startActivity(intent)
                }
            }
            btnLogin.performClick()
        }

        txtEmail.setOnEditorActionListener(editorListener)
        txtPassword.setOnEditorActionListener(editorListener)


        btnLogin.setOnClickListener{

            var intent = Intent(this@LoginIn,MainActivity::class.java)

            val StringEmail = txtEmail.text.toString()
            val StringPassword = txtPassword.text.toString()


            if(validateEmailAddress(txtEmail) && txtPassword.toString().length>=8){
                intent.putExtra("emailIntent",StringEmail)
                saveCredentials(StringEmail , StringPassword)
                allCorrectLoginInfo = true
                startActivity(intent)
            }else{
                Toast.makeText(this@LoginIn,"Enter Correct Details",Toast.LENGTH_SHORT).show()
            }

        }

        btnRegister.setOnClickListener{
            val intentto = Intent(this@LoginIn,RegisterActivity::class.java)
            startActivity(intentto)
        }

    }

    override fun onPause() {
        if(allCorrectLoginInfo){
            finish()
        }
        super.onPause()
    }

    private fun validateEmailAddress (email : EditText):Boolean{
        var StringEmail = txtEmail.text.toString()

        return !StringEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(StringEmail).matches()
    }

    fun actionDone(){
        var intent = Intent(this@LoginIn,MainActivity::class.java)

        val StringEmail = txtEmail.text.toString()

        if(validateEmailAddress(txtEmail) && txtPassword.toString().length>=8){
            intent.putExtra("email",StringEmail)
            startActivity(intent)
        }else{
            Toast.makeText(this@LoginIn,"Password Should be at least 8 Characters",Toast.LENGTH_SHORT).show()
        }
    }

    fun actionNext(){
        if(validateEmailAddress(txtEmail)){
            txtPassword.requestFocus()
        }
    }

    private val editorListener =
        OnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> actionDone()

                EditorInfo.IME_ACTION_NEXT -> actionNext()
                else -> Toast.makeText(
                    this@LoginIn,
                    "Invalid",
                    Toast.LENGTH_SHORT
                ).show()
            }
            false
        }

    fun saveCredentials(email:String,password:String){
        sharedPreferences.edit().putString("email",email).apply()
        sharedPreferences.edit().putString("password",password).apply()
    }
}

