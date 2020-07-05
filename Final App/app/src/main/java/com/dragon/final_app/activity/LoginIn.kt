package com.dragon.final_app.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dragon.final_app.R
import com.dragon.final_app.activity.util.ConnectionManager

import org.json.JSONObject


class LoginIn : AppCompatActivity() {

    lateinit var txtPhone: EditText
    lateinit var txtPassword: EditText
    lateinit var btnLogin: Button
    lateinit var btnRegister: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtForgotPassword: TextView
    var allCorrectLoginInfo: Boolean = false

    var userId = ""
    var name = "name"
    var email = "email"
    var phone = "phone"
    var address = "address"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        val getLoginStatus = sharedPreferences.getBoolean("isLoggedIn", false)
        setContentView(R.layout.activity_login_in)

        txtPhone = findViewById(R.id.txtPhone)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        txtForgotPassword = findViewById(R.id.txtForgotPass)

        if (getLoginStatus) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {

            val StringPassword = txtPassword.text.toString()

            if (ConnectionManager().checkConnectivity(this@LoginIn)) {
                val queue = Volley.newRequestQueue(this@LoginIn)
                val url = "http://13.235.250.119/v2/login/fetch_result/"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", txtPhone.text.toString())
                jsonParams.put("password", txtPassword.text.toString())

                val jsonRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                        try {
                            val dataArray = it.getJSONObject("data")
                            val success = dataArray.getBoolean("success")
                            if (success) {
                                val intent = Intent(this@LoginIn, MainActivity::class.java)
                                allCorrectLoginInfo = true
                                val data = dataArray.getJSONObject("data")
                                userId = data.getString("user_id")
                                name = data.getString("name")
                                email = data.getString("email")
                                phone = data.getString("mobile_number")
                                address = data.getString("address")
                                saveCredentials(
                                    userId,
                                    name,
                                    email,
                                    phone,
                                    address,
                                    StringPassword
                                )
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@LoginIn,
                                    "Incorrect Password/Mobile",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(
                                this@LoginIn,
                                "Some Error Has Occurred!! $e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this@LoginIn, "Volley Error $it", Toast.LENGTH_SHORT)
                            .show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "e168e28418726a"
                        return headers
                    }
                }
                queue.add(jsonRequest)
            } else {
                val dialog = AlertDialog.Builder(this@LoginIn)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Is Not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@LoginIn)
                }

                dialog.create()
                dialog.show()
            }
        }

        btnRegister.setOnClickListener {
            println("Register")
            val intentto = Intent(this@LoginIn, RegisterActivity::class.java)
            startActivity(intentto)
        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginIn, ForgotActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onPause() {
        if (allCorrectLoginInfo) {
            finish()
        }
        super.onPause()
    }

    private fun saveCredentials(
        userId: String,
        name: String,
        email: String,
        phone: String,
        address: String,
        stringPassword: String
    ) {
        sharedPreferences.edit().putString("user_id", userId).apply()
        sharedPreferences.edit().putString("name", name).apply()
        sharedPreferences.edit().putString("password", stringPassword).apply()
        sharedPreferences.edit().putString("email", email).apply()
        sharedPreferences.edit().putString("phone", phone).apply()
        sharedPreferences.edit().putString("address", address).apply()
    }

}




