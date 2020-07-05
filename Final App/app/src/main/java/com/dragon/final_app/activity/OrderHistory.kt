package com.dragon.final_app.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dragon.final_app.R
import com.dragon.final_app.activity.adapter.OrderHistoryAdapter
import com.dragon.final_app.activity.model.OrderHistoryRestaurant
import com.dragon.final_app.activity.util.ConnectionManager
import org.json.JSONException

class OrderHistory : AppCompatActivity() {
    lateinit var layoutManager1: RecyclerView.LayoutManager
    lateinit var menuAdapter1: OrderHistoryAdapter
    lateinit var recyclerViewAllOrders: RecyclerView
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var order_activity_history_Progressdialog: RelativeLayout
    lateinit var order_history_fragment_no_orders: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        recyclerViewAllOrders = findViewById(R.id.recyclerViewAllOrders)

        toolbar = findViewById(R.id.toolBar)

        order_activity_history_Progressdialog =
            findViewById(R.id.order_activity_history_Progressdialog)

        order_history_fragment_no_orders = findViewById(R.id.order_history_fragment_no_orders)
        setUpActionBar()
        setItemsForEachRestaurant()
    }

    fun setItemsForEachRestaurant() {

        layoutManager1 = LinearLayoutManager(this)

        val orderedRestaurantList = ArrayList<OrderHistoryRestaurant>()

        val sharedPreferencess = this.getSharedPreferences(
            getString(R.string.preferences_file_name),
            Context.MODE_PRIVATE
        )

        val user_id = sharedPreferencess.getString("user_id", "")

        if (ConnectionManager().checkConnectivity(this)) {

            order_activity_history_Progressdialog.visibility = View.VISIBLE

            try {

                val queue = Volley.newRequestQueue(this)

                val url = "http://13.235.250.119/v2/orders/fetch_result/$user_id"

                val jsonObjectRequest = object : JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener {

                        val responseJsonObjectData = it.getJSONObject("data")

                        val success = responseJsonObjectData.getBoolean("success")

                        if (success) {

                            val data = responseJsonObjectData.getJSONArray("data")

                            if (data.length() == 0) {

                                Toast.makeText(
                                    this,
                                    "No Orders Placed yet!!!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                order_history_fragment_no_orders.visibility = View.VISIBLE

                            } else {
                                order_history_fragment_no_orders.visibility = View.INVISIBLE



                                for (i in 0 until data.length()) {
                                    val restaurantItemJsonObject = data.getJSONObject(i)

                                    val eachRestaurantObject = OrderHistoryRestaurant(
                                        restaurantItemJsonObject.getString("order_id"),
                                        restaurantItemJsonObject.getString("restaurant_name"),
                                        restaurantItemJsonObject.getString("total_cost"),
                                        restaurantItemJsonObject.getString("order_placed_at")
                                            .substring(0, 10)
                                    )

                                    orderedRestaurantList.add(eachRestaurantObject)

                                    menuAdapter1 = OrderHistoryAdapter(
                                        this,
                                        orderedRestaurantList
                                    )

                                    recyclerViewAllOrders.adapter = menuAdapter1

                                    recyclerViewAllOrders.layoutManager = layoutManager1

                                }

                            }

                        }
                        order_activity_history_Progressdialog.visibility = View.INVISIBLE
                    },
                    Response.ErrorListener {
                        order_activity_history_Progressdialog.visibility = View.INVISIBLE

                        Toast.makeText(
                            this,
                            "Some Error occurred!!!",
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

            } catch (e: JSONException) {
                Toast.makeText(
                    this,
                    "Some Unexpected error occurred!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else {
            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Internet Connection can't be establish!")
            alterDialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                startActivity(settingsIntent)
            }

            alterDialog.setNegativeButton("Exit") { text, listener ->
                finishAffinity()
            }
            alterDialog.setCancelable(false)

            alterDialog.create()
            alterDialog.show()

        }

    }

    fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Previous Orders"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            android.R.id.home -> {
                super.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
