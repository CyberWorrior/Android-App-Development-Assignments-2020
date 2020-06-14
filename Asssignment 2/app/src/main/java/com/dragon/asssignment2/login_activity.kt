package com.dragon.asssignment2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text


class login_activity : AppCompatActivity() {

    lateinit var loginbtn:Button
    lateinit var userTxt:EditText
    lateinit var passTxt:EditText
    lateinit var forgotView:TextView
    lateinit var registerbtn:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        title="LOGIN"

        loginbtn = findViewById(R.id.btnLogin)
        userTxt = findViewById(R.id.txtUser)
        passTxt = findViewById(R.id.txtPassword)
        forgotView = findViewById(R.id.txtforgot)
        registerbtn = findViewById(R.id.txtRegister)

        loginbtn.setOnClickListener{

            var intent = Intent(this@login_activity,DisplayActivity::class.java)
            var StringUser = userTxt.text.toString()
            var StringPass = passTxt.text.toString()
            if(StringUser!="" && StringPass!=""){
                Toast.makeText(this@login_activity,"Logged In",Toast.LENGTH_SHORT).show()
                intent.putExtra("username",StringUser)
                intent.putExtra("password",StringPass)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@login_activity,"Please Enter Details",Toast.LENGTH_SHORT).show()
            }

        }

        forgotView.setOnClickListener{
            var intent = Intent(this@login_activity,forgotActivity::class.java)
            startActivity(intent)
        }

        registerbtn.setOnClickListener{
            var intent = Intent (this@login_activity,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
