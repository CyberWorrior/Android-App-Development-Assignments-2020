package com.dragon.final_app.activity.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dragon.final_app.R
import com.dragon.final_app.activity.adapter.HomeRecyclerAdapter
import com.dragon.final_app.activity.model.Rest
import com.dragon.final_app.activity.util.ConnectionManager
import org.json.JSONException


class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    var restId: String? = "100"
    var RestList = arrayListOf<Rest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        val obj = it.getJSONObject("data")
                        val success = obj.getBoolean("success")
                        if (success) {
                            progressLayout.visibility = View.GONE
                            val dataObj = it.getJSONObject("data")
                            val data = dataObj.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val restJsonObject = data.getJSONObject(i)
                                val restobject = Rest(
                                    restJsonObject.getString("id"),
                                    restJsonObject.getString("name"),
                                    restJsonObject.getString("rating"),
                                    restJsonObject.getString("cost_for_one"),
                                    restJsonObject.getString("image_url")
                                )

                                RestList.add(restobject)

                                recyclerAdapter = HomeRecyclerAdapter(activity as Context, RestList)

                                recyclerHome.layoutManager = layoutManager

                                recyclerHome.adapter = recyclerAdapter


                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Has Occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(activity as Context, "JSON:$e", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
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
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Success")
            dialog.setMessage("Internet Connection Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                //closes the app
                ActivityCompat.finishAffinity(activity as Activity)
            }

            dialog.create()
            dialog.show()
        }
        return view
    }

}
