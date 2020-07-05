package com.dragon.final_app.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
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

class OtpActivity : AppCompatActivity() {


    lateinit var etOtp: EditText
    lateinit var etNewPass: EditText
    lateinit var etConfirmPass: EditText
    lateinit var btnSubmit: Button
    var success: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        etOtp = findViewById(R.id.etOtp)
        etNewPass = findViewById(R.id.etNewPassword)
        etConfirmPass = findViewById(R.id.etCPassword)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this@OtpActivity)) {
                try {
                    val queue = Volley.newRequestQueue(this@OtpActivity)
                    val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                    val jsonParams = JSONObject()

                    if (intent != null) {
                        jsonParams.put("mobile_number", intent.getStringExtra("mobileNumber"))
                    }
                    jsonParams.put("password", etNewPass.text.toString())
                    jsonParams.put("otp", etOtp.text.toString())

                    val jsonRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            val data = it.getJSONObject("data")
                            success = data.getBoolean("success")
                            if (success) {
                                Toast.makeText(
                                    this@OtpActivity,
                                    "Password Has Successfully Changed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@OtpActivity, LoginIn::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@OtpActivity, "Incorrect", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(this@OtpActivity, "Volley Error $it", Toast.LENGTH_SHORT)
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

                } catch (e: Exception) {
                    Toast.makeText(
                        this@OtpActivity,
                        "Some Error Has Occurred!! $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val dialog = AlertDialog.Builder(this@OtpActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Is Not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@OtpActivity)
                }

                dialog.create()
                dialog.show()
            }
        }
    }

    override fun onPause() {
        if (success) {
            finish()
        }
        super.onPause()
    }
}
