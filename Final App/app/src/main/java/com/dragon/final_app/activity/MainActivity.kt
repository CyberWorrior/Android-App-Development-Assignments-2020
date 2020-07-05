package com.dragon.final_app.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dragon.final_app.R
import com.dragon.final_app.activity.fragment.FaqsFragment
import com.dragon.final_app.activity.fragment.FavouriteFragment
import com.dragon.final_app.activity.fragment.HomeFragment
import com.dragon.final_app.activity.fragment.ProfileFragment
import com.dragon.final_app.activity.util.ConnectionManager
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var sharedPreferences: SharedPreferences


    var previousMenuItem: MenuItem? = null

    var userId = ""
    var userName = "name"
    var userEmail = "email"
    var userPhone = "phone"
    var userAddress = "address"
    var noOfTimes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        noOfTimes = sharedPreferences.getInt("opened", 0)
        savePreferences()

        println("No of times ${sharedPreferences.getInt("opened", 15)}")

        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinateLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)
        progressLayout.visibility = View.GONE

        setUpActionBar()
        openHome()


        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

        //Display Menu Button
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        if(sharedPreferences.getBoolean("isLoggedIn", false)){
            getUserDetails()
        }

        navigationView.getHeaderView(0).findViewById<TextView>(R.id.txtUserName).text =
            sharedPreferences.getString("name", "Name")
        navigationView.getHeaderView(0).findViewById<TextView>(R.id.txtUserPhone).text =
            sharedPreferences.getString("phone", "phone")

//        getUserDetails()
        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.home -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment()).commit()
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.favouriteRest -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavouriteFragment()).commit()
                    supportActionBar?.title = "Favourite Restaurants"
                    drawerLayout.closeDrawers()


                }

                R.id.faqs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FaqsFragment()).commit()
                    supportActionBar?.title = "FAQS"
                    drawerLayout.closeDrawers()
                }

                R.id.orderHistory -> {
                    drawerLayout.closeDrawers()
                    val intent = Intent(this, OrderHistory::class.java)
                    startActivity(intent)
                }

                R.id.logOut -> {
                    drawerLayout.closeDrawers()
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to exit")
                    dialog.setPositiveButton("YES") { text, listener ->
                        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                        sharedPreferences.edit().putString("phone", "").apply()
                        sharedPreferences.edit().putString("password", "").apply()
                        sharedPreferences.edit().putString("email", "").apply()
                        sharedPreferences.edit().putString("address", "").apply()
                        val i = Intent(this@MainActivity, LoginIn::class.java)
                        startActivity(i)
                        this@MainActivity.finish()
                    }
                    dialog.setNegativeButton("NO") { text, listener ->

                    }

                    dialog.create()
                    dialog.show()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    //pass to profile Argument
    public fun getEmail(): String? {
        return sharedPreferences.getString("email", "Your Email")
    }

    public fun getAddress(): String? {
        return sharedPreferences.getString("address", "No Address")
    }

    public fun getPhone(): String? {
        return sharedPreferences.getString("phone", "No Phone")
    }

    public fun getName(): String? {
        return sharedPreferences.getString("name", "No Name")
    }


    fun getUserDetails() {
        if (intent != null) {
            val phoneNo = sharedPreferences.getString("phone", "Phone Number")
            val pass = sharedPreferences.getString("password", "Password")

            if (ConnectionManager().checkConnectivity(this)) {
                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/login/fetch_result/"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", phoneNo)
                jsonParams.put("password", pass)

                val jsonRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                        try {
                            val dataArray = it.getJSONObject("data")
                            val success = dataArray.getBoolean("success")
                            if (success) {
                                progressLayout.visibility = View.GONE
                                val data = dataArray.getJSONObject("data")
                                userId = data.getString("user_id")
                                userName = data.getString("name")
                                userEmail = data.getString("email")
                                userPhone = data.getString("mobile_number")
                                userAddress = data.getString("address")
                                if (pass != null) {
                                    saveCredentials(
                                        userId,
                                        userName,
                                        userEmail,
                                        userPhone,
                                        userAddress,
                                        pass
                                    )
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Unable to get User details",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(
                                this,
                                "Some Error Has Occurred!! $e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT)
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
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Is Not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this)
                }

                dialog.create()
                dialog.show()
            }
        }
    }

    //Enable costum toolbar using first line
    fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    //Enable menu button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frameLayout,
                HomeFragment()
            ).commit()

        supportActionBar?.title = "Home"
        navigationView.setCheckedItem(R.id.home)
    }

    public fun getNoOfTimesOpened(): Int {
        return sharedPreferences.getInt("opened", 0)
    }

    fun savePreferences() {
        sharedPreferences.edit().putInt("opened", ++noOfTimes).apply()
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)

        when (frag) {
            !is HomeFragment -> openHome()

            else -> super.onBackPressed()
        }
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