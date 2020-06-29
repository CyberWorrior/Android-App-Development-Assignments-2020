package com.dragon.login_register.activity.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.dragon.login_register.R
import com.dragon.login_register.activity.adapter.FavouriteRecyclerAdapter
import com.dragon.login_register.activity.database.RestDatabase
import com.dragon.login_register.activity.database.RestEntity
import kotlinx.android.synthetic.main.activity_main.*


class FavouriteFragment : Fragment() {

    lateinit var recyclerFav: RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter:FavouriteRecyclerAdapter
    lateinit var pullToRefresh:SwipeRefreshLayout

    var dbRestList = listOf<RestEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFav = view.findViewById(R.id.recyclerFav)
        pullToRefresh = view.findViewById(R.id.pullToRefresh)

        layoutManager = LinearLayoutManager(activity)

        dbRestList = RetrieveFavourites(activity as Context).execute().get()

        if(activity!=null){
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context,dbRestList)
            recyclerFav.adapter = recyclerAdapter
            recyclerFav.layoutManager = layoutManager
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

    class RetrieveFavourites(val context:Context): AsyncTask<Void, View, List<RestEntity>>(){
        override fun doInBackground(vararg params: Void?): List<RestEntity> {
            val db = Room.databaseBuilder(context,RestDatabase::class.java,"rests-db").build()

            println("Rests are ${db.restDao().getAllRests()}")

            return db.restDao().getAllRests()
        }
    }



}
