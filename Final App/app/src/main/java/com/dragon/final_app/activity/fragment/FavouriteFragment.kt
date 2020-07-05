package com.dragon.final_app.activity.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dragon.final_app.R
import com.dragon.final_app.activity.adapter.FavouriteRecyclerAdapter
import com.dragon.final_app.activity.database.RestDatabase
import com.dragon.final_app.activity.database.RestEntity


class FavouriteFragment : Fragment() {

    lateinit var recyclerFav: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    lateinit var pullToRefresh: SwipeRefreshLayout

    var dbRestList = listOf<RestEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFav = view.findViewById(R.id.recyclerFav)
        pullToRefresh = view.findViewById(R.id.pullToRefresh)

        layoutManager = LinearLayoutManager(activity)

        dbRestList = RetrieveFavourites(activity as Context).execute().get()

        if (activity != null) {
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbRestList)
            recyclerFav.layoutManager = layoutManager
            recyclerFav.adapter = recyclerAdapter

        }

        pullToRefresh.setOnRefreshListener {
            val currentFagment: Fragment? = fragmentManager?.findFragmentById(R.id.frameLayout)
            val fragmentTransaction = fragmentManager?.beginTransaction()
            if (fragmentTransaction != null) {
                if (currentFagment != null) {
                    fragmentTransaction.detach(currentFagment)
                    fragmentTransaction.attach(currentFagment).commit()
                }
            }

        }

        return view
    }


    class RetrieveFavourites(val context: Context) : AsyncTask<Void, View, List<RestEntity>>() {
        override fun doInBackground(vararg params: Void?): List<RestEntity> {
            val db = Room.databaseBuilder(context, RestDatabase::class.java, "rests-db").build()

            println("Rests are ${db.restDao().getAllRests()}")

            return db.restDao().getAllRests()
        }
    }


}
