package com.dragon.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dragon.bookhub.R
import com.dragon.bookhub.adapter.DashboardRecyclerAdapter
import com.dragon.bookhub.model.Book
import com.dragon.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class DashbordFragment : Fragment() {
//    val bookInfoList = arrayListOf<Book>(
//        Book("1", "P.S. I love You", "Cecelia Ahern", "4.5","Rs. 299","https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1392344536l/366522.jpg")
//    )

    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar


    /* val bookInfoList = arrayListOf<Book>(
         Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", R.drawable.ps_ily),
         Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby),
         Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
         Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
         Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
         Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
         Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
         Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn),
         Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
         Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)
     )

 */

    val bookInfoList = arrayListOf<Book>()
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var btnCheckInternet:Button
    lateinit var recyclerDashbord: RecyclerView
    lateinit var layoutManger: RecyclerView.LayoutManager

    var ratingComparator = Comparator<Book>{book1,book2 ->

        if(book1.bookRating.compareTo(book2.bookRating,true)==0){
            //sort according to name if book rating is same
            book1.bookName.compareTo(book2.bookName,true)
        }else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashbord, container, false)

        setHasOptionsMenu(true)

        recyclerDashbord = view.findViewById(R.id.recyclerDashboard)

        btnCheckInternet = view.findViewById(R.id.btnCheckInternet)

        layoutManger = LinearLayoutManager(activity)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.VISIBLE

        btnCheckInternet.setOnClickListener{
            if(ConnectionManager().checkConnectivity(activity as Context)){
                //Internet is available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok"){text,listener ->

                }
                dialog.setNegativeButton("Cancel"){text,listener ->

                }

                dialog.create()
                dialog.show()
            }else{
                //Internet Not Available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Is Not Found")
                dialog.setPositiveButton("Ok"){text,listener ->

                }
                dialog.setNegativeButton("Cancel"){text,listener ->

                }

                dialog.create()
                dialog.show()
            }
        }




        //Getting Data From Internet using Volley

        if(ConnectionManager().checkConnectivity(activity as Context)) {
            var queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v1/book/fetch_books/"
            val jsonObjectRequest = object:JsonObjectRequest(Request.Method.GET,url,null,Response.Listener {
                //Here we will handle the response
                println("Response is $it")
                val success = it.getBoolean("success")
                try {
                    progressLayout.visibility = View.GONE
                    if(success){
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )

                            bookInfoList.add(bookObject)
                            recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList )

                            recyclerDashbord.adapter = recyclerAdapter

                            recyclerDashbord.layoutManager = layoutManger

                        }
                    }else{
                        Toast.makeText(activity as Context,"Some Error Has Occurred!!",Toast.LENGTH_SHORT).show()
                    }
                }catch (e: JSONException){
                    Toast.makeText(activity as Context,"Some Error Has Occurred!!",Toast.LENGTH_SHORT).show()
                }

            },Response.ErrorListener {
                //Here we will handle the errors
                if(activity != null){
                    Toast.makeText(activity as Context,"Volley Error Occurred!!",Toast.LENGTH_SHORT).show()
                }

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "e168e28418726a"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Success")
            dialog.setMessage("Internet Connection Found")
            dialog.setPositiveButton("Open Settings"){text,listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Cancel"){text,listener ->
                //closes the app
                ActivityCompat.finishAffinity(activity as Activity)
            }

            dialog.create()
            dialog.show()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if(id == R.id.action_sort){
             Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

}
