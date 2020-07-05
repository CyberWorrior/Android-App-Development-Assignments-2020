package com.dragon.final_app.activity.fragment


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.dragon.final_app.R
import com.dragon.final_app.activity.MainActivity
import com.dragon.final_app.activity.database.RestDatabase

class ProfileFragment : Fragment() {

    lateinit var txtDisplayEmail: TextView
    lateinit var txtNoOfTimes: TextView
    lateinit var txtNoOfFav: TextView
    lateinit var txtName: TextView
    lateinit var txtAddress: TextView
    lateinit var txtPhone: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myproflie, container, false)

        txtDisplayEmail = view.findViewById(R.id.txtDisplayEmail)
        txtNoOfTimes = view.findViewById(R.id.txtNoOfTimes)
        txtNoOfFav = view.findViewById(R.id.txtNoOfFav)
        txtName = view.findViewById(R.id.txtDisplayName)
        txtAddress = view.findViewById(R.id.txtDisplayAddress)
        txtPhone = view.findViewById(R.id.txtDisplayPhone)


        val activity: MainActivity = activity as MainActivity

        txtDisplayEmail.text = activity.getEmail()
        txtPhone.text = activity.getPhone()
        txtName.text = activity.getName()
        txtAddress.text = activity.getAddress()
        txtNoOfTimes.text = activity.getNoOfTimesOpened().toString()

        txtNoOfFav.text = DBCountAsyncTask(activity as Context).execute().get().toString()

        return view
    }

}

class DBCountAsyncTask(val context: Context) : AsyncTask<Void, Void, Int>() {
    override fun doInBackground(vararg params: Void?): Int {
        val db = Room.databaseBuilder(context, RestDatabase::class.java, "rests-db").build()

        return db.restDao().getFavouriteRestaurantCount()
    }

}

