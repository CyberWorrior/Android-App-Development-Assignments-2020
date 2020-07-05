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

class ForgotActivity : AppCompatActivity() {

    lateinit var etPhone: EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button
    var success: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        etPhone = findViewById(R.id.etForgotPhone)
        etEmail = findViewById(R.id.etForgotEmail)
        btnNext = findViewById(R.id.btnForgetNext)

        btnNext.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this@ForgotActivity)) {
                val queue = Volley.newRequestQueue(this@ForgotActivity)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", etPhone.text.toString())
                jsonParams.put("email", etEmail.text.toString())

                val jsonRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                        try {
                            val data = it.getJSONObject("data")
                            success = data.getBoolean("success")
                            if (success) {
                                //Set Intent Here to OTP Page
                                val intent = Intent(this@ForgotActivity, OtpActivity::class.java)
                                intent.putExtra("mobileNumber", etPhone.text.toString())
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@ForgotActivity, "Not Found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@ForgotActivity,
                                "Some Error Has Occurred!! $e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }, Response.ErrorListener {
                        Toast.makeText(this@ForgotActivity, "Volley Error $it", Toast.LENGTH_SHORT)
                            .show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["content-type"] = "application/json"
                        headers["token"] = "e168e28418726a"
                        return headers
                    }
                }
                queue.add(jsonRequest)
            } else {
                val dialog = AlertDialog.Builder(this@ForgotActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Is Not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@ForgotActivity)
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
