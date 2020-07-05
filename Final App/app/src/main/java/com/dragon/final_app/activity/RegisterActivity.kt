package com.dragon.final_app.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dragon.final_app.R
import com.dragon.final_app.activity.util.ConnectionManager
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var etRePassword: EditText
    lateinit var btnRegButton: Button
    lateinit var etAddress: EditText
    lateinit var etPhone: EditText
    lateinit var sharedPreferences: SharedPreferences
    var success: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etRegName)
        etEmail = findViewById(R.id.etRegEmail)
        etPassword = findViewById(R.id.etRegPassword)
        etRePassword = findViewById(R.id.etRegRePassword)
        btnRegButton = findViewById(R.id.btnRegButton)
        etAddress = findViewById(R.id.etRegAddress)
        etPhone = findViewById(R.id.etRegPhone)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        btnRegButton.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this@RegisterActivity)) {
                val queue = Volley.newRequestQueue(this@RegisterActivity)
                val url = "http://13.235.250.119/v2/register/fetch_result"

                val jsonParams = JSONObject()
                if (etPassword.text.toString() == etRePassword.text.toString() && validateEmailAddress(
                        etEmail
                    )
                ) {
                    jsonParams.put("name", etName.text.toString())
                    jsonParams.put("mobile_number", etPhone.text.toString())
                    jsonParams.put("password", etPassword.text.toString())
                    jsonParams.put("address", etAddress.text.toString())
                    jsonParams.put("email", etEmail.text.toString())
                    println("JsonParams is $jsonParams")
                    val jsonRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {
                                val dataArray = it.getJSONObject("data")
                                success = dataArray.getBoolean("success")
                                if (success) {
//                                val userJson = dataArray.getJSONObject("data")
                                    val intent = Intent(this@RegisterActivity, LoginIn::class.java)
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Registration Successful,Pls Login to Continue..",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(intent)
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Some Error Has Occurred!! $e",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Volley Error Occurred!! $it",
                                Toast.LENGTH_SHORT
                            ).show()
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
                    Toast.makeText(
                        this@RegisterActivity,
                        "Enter Proper Details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val dialog = AlertDialog.Builder(this@RegisterActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Is Not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                }

                dialog.create()
                dialog.show()
            }
        }
    }

    private fun validateEmailAddress(email: EditText): Boolean {
        var StringEmail = email.text.toString()

        return !StringEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(StringEmail).matches()
    }

    override fun onPause() {
        if (success == true) {
            finish()
        }
        super.onPause()
    }
}
