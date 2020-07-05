package com.dragon.final_app.activity

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dragon.final_app.R
import com.dragon.final_app.activity.Cartdatabase.FoodDatabase
import com.dragon.final_app.activity.Cartdatabase.FoodEntity
import com.dragon.final_app.activity.adapter.CartRecyclerAdapter
import com.dragon.final_app.activity.model.CartItems
import com.dragon.final_app.activity.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartPageActivity : AppCompatActivity() {
    lateinit var recyclerCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var btnPlaceOrder: Button
    lateinit var toolbar: Toolbar
    lateinit var txtCartRestName: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var progressLayout: RelativeLayout
    var restId = "100"
    var totalAmount = 0
    var title = "Restaurant Name"
    var selectedItemsId = arrayListOf<String>()

    var dbFoodList = listOf<FoodEntity>()

    var cartListItems = arrayListOf<CartItems>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cart_page)

        recyclerCart = findViewById(R.id.recyclerCart)
        toolbar = findViewById(R.id.toolbar)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        txtCartRestName = findViewById(R.id.txtCartRestName)
        progressLayout = findViewById(R.id.progressLayout)
        layoutManager = LinearLayoutManager(this@CartPageActivity)
        progressLayout.visibility = View.VISIBLE

        dbFoodList = RetrieveCart(this@CartPageActivity).execute().get()
        setUpActionBar()
        if (intent != null) {
            restId = intent.getStringExtra("restaurantId")
            title = intent.getStringExtra("restName")
            selectedItemsId = intent.getStringArrayListExtra("selectedItemsId")
        }

        txtCartRestName.text = title



        btnPlaceOrder.setOnClickListener {
            sharedPreferences =
                getSharedPreferences(
                    getString(R.string.preferences_file_name),
                    Context.MODE_PRIVATE
                )

            if (ConnectionManager().checkConnectivity(this@CartPageActivity)) {
                try {
                    val foodJsonArray = JSONArray()
                    for (foodItem in selectedItemsId) {
                        val singleItemObject = JSONObject()
                        singleItemObject.put("food_item_id", foodItem)
                        foodJsonArray.put(singleItemObject)
                    }

                    val sendOrder = JSONObject()
                    sendOrder.put("user_id", sharedPreferences.getString("user_id", ""))
                    sendOrder.put("restaurant_id", restId)
                    sendOrder.put("total_cost", totalAmount)
                    sendOrder.put("food", foodJsonArray)

                    val queue = Volley.newRequestQueue(this@CartPageActivity)
                    val url = "http://13.235.250.119/v2/place_order/fetch_result/"

                    val jsonObjectRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, sendOrder, Response.Listener {
                            val responseJsonObjectData = it.getJSONObject("data")
                            val success = responseJsonObjectData.getBoolean("success")
                            if (success) {
                                Toast.makeText(
                                    this@CartPageActivity,
                                    "Order Placed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                createNotification()
                                val intent = Intent(this, OrderPlaced::class.java)
                                startActivity(intent)
                                finishAffinity()
                            } else {
                                Toast.makeText(
                                    this@CartPageActivity,
                                    "Some Error Has Occurred!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, Response.ErrorListener {
                            if (this@CartPageActivity != null) {
                                Toast.makeText(
                                    this@CartPageActivity,
                                    "Volley Error Occurred!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "e168e28418726a"
                            return headers
                        }
                    }
                    jsonObjectRequest.setRetryPolicy(
                        DefaultRetryPolicy(
                            15000,
                            1,
                            1f
                        )
                    )
                    queue.add(jsonObjectRequest)
                } catch (e: Exception) {
                    Toast.makeText(
                        this@CartPageActivity,
                        "Some Error Has Occurred!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val dialog = AlertDialog.Builder(this@CartPageActivity)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    this@CartPageActivity?.finish()

                }
                dialog.setNegativeButton("Cancel") { text, listener ->
                    //closes the app
                    ActivityCompat.finishAffinity(this@CartPageActivity)
                }

                dialog.create()
                dialog.show()
            }
        }
        FetchData()


    }

    fun FetchData() {
        if (ConnectionManager().checkConnectivity(this@CartPageActivity)) {
            var queue = Volley.newRequestQueue(this@CartPageActivity)
            var url = "http://13.235.250.119/v2/restaurants/fetch_result/$restId"

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            progressLayout.visibility = View.GONE
                            val foodMenuData = data.getJSONArray("data")
                            cartListItems.clear()
                            totalAmount = 0

                            for (i in 0 until foodMenuData.length()) {
                                val cartItemJsonObject = foodMenuData.getJSONObject(i)
                                if (selectedItemsId.contains(cartItemJsonObject.getString("id"))) {
                                    val menuObject = CartItems(
                                        cartItemJsonObject.getString("id"),
                                        cartItemJsonObject.getString("name"),
                                        cartItemJsonObject.getString("cost_for_one"),
                                        cartItemJsonObject.getString("restaurant_id")
                                    )
                                    totalAmount += cartItemJsonObject.getString("cost_for_one")
                                        .toString().toInt()
                                    cartListItems.add(menuObject)

                                }
                            }
                            if (this@CartPageActivity != null) {
                                recyclerAdapter =
                                    CartRecyclerAdapter(this@CartPageActivity, cartListItems)
                                recyclerCart.adapter = recyclerAdapter
                                recyclerCart.layoutManager = layoutManager

                            }
                            btnPlaceOrder.text = "Place Order(Total: Rs. $totalAmount)"


                        } else {
                            Toast.makeText(
                                this@CartPageActivity,
                                "Some Error Has Occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@CartPageActivity,
                            "Some Error Has Occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    if (this@CartPageActivity != null) {
                        Toast.makeText(
                            this@CartPageActivity,
                            "Volley Error Occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
            val dialog = AlertDialog.Builder(this@CartPageActivity)
            dialog.setTitle("Success")
            dialog.setMessage("Internet Connection Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this@CartPageActivity?.finish()

            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                //closes the app
                ActivityCompat.finishAffinity(this@CartPageActivity)
            }

            dialog.create()
            dialog.show()
        }
    }

    class RetrieveCart(val context: Context) : AsyncTask<Void, View, List<FoodEntity>>() {
        override fun doInBackground(vararg params: Void?): List<FoodEntity> {
            val db = Room.databaseBuilder(context, FoodDatabase::class.java, "cart-db").build()

            return db.foodDao().getAllCartItems()
        }

    }

    fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this@CartPageActivity.finish()
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    fun createNotification() {
        val notificationId = 1;
        val channelId = "personal_notification"

        val v = longArrayOf(2000, 500)
        val myIntent = Intent(this, OrderHistory::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, myIntent, 0)

        val notificationBulider = NotificationCompat.Builder(this, channelId)
        notificationBulider.setSmallIcon(R.drawable.rest_logo)
        notificationBulider.setContentTitle("Order Placed")
        notificationBulider.setContentText("Your order has been successfully placed!")
        notificationBulider.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Ordered from $title and amounting to Rs.$totalAmount")
        )
        notificationBulider.priority = NotificationCompat.PRIORITY_HIGH
        notificationBulider.setAutoCancel(true)
        notificationBulider.setWhen(System.currentTimeMillis())
        notificationBulider.setTicker("Order Placed")
        notificationBulider.setContentIntent(pendingIntent)
        notificationBulider.setVibrate(v)


        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(notificationId, notificationBulider.build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)//less than oreo
        {
            val name = "Order Placed"
            val description = "Your order has been successfully placed!"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val notificationChannel = NotificationChannel(channelId, name, importance)

            notificationChannel.description = description

            val notificationManager =
                (getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
}
