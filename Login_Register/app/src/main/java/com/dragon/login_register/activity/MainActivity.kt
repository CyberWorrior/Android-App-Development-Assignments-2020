package com.dragon.login_register.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.dragon.login_register.R
import com.dragon.login_register.activity.fragment.FaqsFragment
import com.dragon.login_register.activity.fragment.FavouriteFragment
import com.dragon.login_register.activity.fragment.HomeFragment
import com.dragon.login_register.activity.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout:FrameLayout
    lateinit var navigationView: NavigationView

    lateinit var sharedPreferences: SharedPreferences

    var actEmail = "Your Email"

    var noOfTimes  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        noOfTimes  = sharedPreferences.getInt("opened",0)
        savePreferences()

        println("No of times ${sharedPreferences.getInt("opened",15)}")

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinateLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)

        setUpActionBar()
        openHome()

        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

        //Display Menu Button
        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.open_drawer,R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        //get email from login Activity
        if(sharedPreferences.getBoolean("isLoggedIn",false)){

            actEmail = sharedPreferences.getString("email","Your Email").toString()
        }

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
                    openHome()
                    drawerLayout.closeDrawers()
                }

                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,ProfileFragment()).commit()
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.favouriteRest ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,FavouriteFragment()).commit()
                    supportActionBar?.title = "Favourite Restaurants"
                    drawerLayout.closeDrawers()
                }

                R.id.faqs ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,FaqsFragment()).commit()
                    supportActionBar?.title = "FAQS"
                    drawerLayout.closeDrawers()
                }

                R.id.logOut ->{
                    sharedPreferences.edit().putBoolean("isLoggedIn",false)
                    sharedPreferences.edit().putString("email","").apply()
                    sharedPreferences.edit().putString("password","").apply()
                    val i = Intent(this@MainActivity,LoginIn::class.java)
                    startActivity(i)
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    //pass to profile Argument
    public fun getEmail():String{
        return actEmail
    }


    //Enable costum toolbar using first line
    fun setUpActionBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    //Enable menu button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId

        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frameLayout,
                HomeFragment()
            ).commit()

        supportActionBar?.title = "Home"
        navigationView.setCheckedItem(R.id.home)
    }

    public fun getNoOfTimesOpened():Int{
        return sharedPreferences.getInt("opened",0)
    }

    fun savePreferences(){
        sharedPreferences.edit().putInt("opened",++noOfTimes).apply()
    }

    override fun onPause() {
        finish()
        super.onPause()
    }
}