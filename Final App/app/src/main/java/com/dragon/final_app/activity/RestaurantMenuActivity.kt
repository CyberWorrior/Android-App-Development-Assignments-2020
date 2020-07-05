package com.dragon.final_app.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dragon.final_app.R
import com.dragon.final_app.activity.adapter.DescriptionRecyclerAdapter
import com.dragon.final_app.activity.model.Food
import com.dragon.final_app.activity.util.ConnectionManager

class RestaurantMenuActivity : AppCompatActivity() {

    lateinit var recyclerDesc: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DescriptionRecyclerAdapter
    lateinit var toolbar: Toolbar
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var btnProceedToCart: Button
    lateinit var favDescMark: ImageView

    var title: String = "No Title"
    var restId: String = "100"
    var foodList = arrayListOf<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)
        recyclerDesc = findViewById(R.id.recyclerDesc)
        toolbar = findViewById(R.id.toolbar)
        btnProceedToCart = findViewById(R.id.btnProceedToCart)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        favDescMark = findViewById(R.id.favDescMark)
        progressLayout.visibility = View.VISIBLE
        DBCartAsyncTask(this@RestaurantMenuActivity, null, 4).execute()
        layoutManager = LinearLayoutManager(this@RestaurantMenuActivity)
        if (intent != null) {
            restId = intent.getStringExtra("restId")
            title = intent.getStringExtra("restName")
        }

        setUpActionBar()

        if (ConnectionManager().checkConnectivity(this@RestaurantMenuActivity)) {
            val queue = Volley.newRequestQueue(this@RestaurantMenuActivity)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restId"

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            progressLayout.visibility = View.GONE
                            val dataArray = data.getJSONArray("data")
                            for (i in 0 until dataArray.length()) {
                                val foodJsonObject = dataArray.getJSONObject(i)
                                val foodObject = Food(
                                    foodJsonObject.getString("id").toInt(),
                                    foodJsonObject.getString("name"),
                                    foodJsonObject.getString("cost_for_one"),
                                    foodJsonObject.getString("restaurant_id").toInt()
                                )

                                foodList.add(foodObject)

                                recyclerAdapter = DescriptionRecyclerAdapter(
                                    this@RestaurantMenuActivity,
                                    foodList,
                                    btnProceedToCart,
                                    restId,
                                    title
                                )
                                recyclerDesc.layoutManager = layoutManager
                                recyclerDesc.adapter = recyclerAdapter

                            }
                        } else {
                            Toast.makeText(
                                this@RestaurantMenuActivity,
                                "Some Error Has Occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@RestaurantMenuActivity, "JSON:$e", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this@RestaurantMenuActivity,
                        "Volley Error Occurred!!",
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
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(this@RestaurantMenuActivity)
            dialog.setTitle("Success")
            dialog.setMessage("Internet Connection Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this@RestaurantMenuActivity?.finish()

            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                //closes the app
                ActivityCompat.finishAffinity(this@RestaurantMenuActivity)
            }

            dialog.create()
            dialog.show()
        }


    }

    fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (recyclerAdapter.getSelectedItemCount() > 0) {

            val dialog = AlertDialog.Builder(this@RestaurantMenuActivity)
            dialog.setTitle("Cart")
            dialog.setMessage("Going Back will delete Cart Items")
            dialog.setPositiveButton("Ok") { text, listener ->
                val deleteCart = DBCartAsyncTask(this@RestaurantMenuActivity, null, 4).execute()
                val isDeleted = deleteCart.get()
                if (isDeleted) {
                    recyclerAdapter.setSelectedItemCount(0)
                    this@RestaurantMenuActivity.finish()
                }
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
            }

            dialog.create()
            dialog.show()
        } else {
            finish()
            super.onOptionsItemSelected(item)
        }
        return false
    }

    override fun onBackPressed() {
        if (recyclerAdapter.getSelectedItemCount() > 0) {
            val dialog = AlertDialog.Builder(this@RestaurantMenuActivity)
            dialog.setTitle("Cart")
            dialog.setMessage("Going Back will delete Cart Items")
            dialog.setPositiveButton("Ok") { text, listener ->

                val deleteCart = DBCartAsyncTask(this@RestaurantMenuActivity, null, 4).execute()
                val isDeleted = deleteCart.get()
                if (isDeleted) {
                    recyclerAdapter.setSelectedItemCount(0)
                    this@RestaurantMenuActivity.finish()
                    super.onBackPressed()
                }
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
            }

            dialog.create()
            dialog.show()
        } else {
            super.onBackPressed()
        }
    }
}
