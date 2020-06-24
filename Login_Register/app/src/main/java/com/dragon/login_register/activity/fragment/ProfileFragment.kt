package com.dragon.login_register.activity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.dragon.login_register.R
import com.dragon.login_register.activity.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var txtDisplayEmail:TextView
    lateinit var txtNoOfTimes:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myproflie, container, false)

        txtDisplayEmail = view.findViewById(R.id.txtDisplayEmail)
        txtNoOfTimes = view.findViewById(R.id.txtNoOfTimes)


        val activity : MainActivity = activity as MainActivity

        txtDisplayEmail.text = activity.getEmail()
        txtNoOfTimes.text = activity.getNoOfTimesOpened().toString()

        return view
    }

}
