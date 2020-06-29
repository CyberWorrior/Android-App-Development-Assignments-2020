package com.dragon.login_register.activity.fragment

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.dragon.login_register.R
import com.dragon.login_register.activity.MainActivity
import com.dragon.login_register.activity.adapter.FavouriteRecyclerAdapter
import com.dragon.login_register.activity.adapter.getFavourtiesItemCount
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var txtDisplayEmail:TextView
    lateinit var txtNoOfTimes:TextView
    lateinit var txtNoOfFav: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myproflie, container, false)

        txtDisplayEmail = view.findViewById(R.id.txtDisplayEmail)
        txtNoOfTimes = view.findViewById(R.id.txtNoOfTimes)
        txtNoOfFav = view.findViewById(R.id.txtNoOfFav)

        val getFavourtiesItemCount = getFavourtiesItemCount


        val activity : MainActivity = activity as MainActivity

        txtDisplayEmail.text = activity.getEmail()
        txtNoOfTimes.text = activity.getNoOfTimesOpened().toString()
        txtNoOfFav.text = getFavourtiesItemCount.toString()


        return view
    }
}

