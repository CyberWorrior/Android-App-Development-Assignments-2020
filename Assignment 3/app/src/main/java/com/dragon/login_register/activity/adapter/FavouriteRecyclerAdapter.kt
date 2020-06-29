package com.dragon.login_register.activity.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dragon.login_register.R
import com.dragon.login_register.activity.AddToFavourties
import com.dragon.login_register.activity.database.RestDatabase
import com.dragon.login_register.activity.database.RestEntity
import com.dragon.login_register.activity.model.Rest
import com.squareup.picasso.Picasso


var getFavourtiesItemCount:Int = 0
class FavouriteRecyclerAdapter(val context: Context, val itemList:List<RestEntity>): RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtRestName:TextView = view.findViewById(R.id.txtFavRestName)
        val txtRestRating:TextView = view.findViewById(R.id.txtFavRating)
        val txtPrice:TextView = view.findViewById(R.id.txtFavPrice)
        val imgFavImage: ImageView = view.findViewById(R.id.imgFavRestImage)
        val favRed :ImageView = view.findViewById(R.id.imgFavFavMark)
        var restID = "100"


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        getFavourtiesItemCount = itemList.size
        return getFavourtiesItemCount
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        var rest = itemList[position]
        holder.restID = rest.rest_id.toString()
        holder.txtRestName.text = rest.restName
        holder.txtRestRating.text = rest.rating
        holder.txtPrice.text = rest.price
        Picasso.get().load(rest.restImage).error(R.drawable.ic_launcher_background).into(holder.imgFavImage)
        val restEntity = RestEntity(
            holder.restID.toInt(),
            holder.txtRestName.text.toString(),
            holder.txtRestRating.text.toString(),
            holder.txtPrice.text.toString(),
            rest.restImage
        )

        val checkFav = DBAsyncTask(context,restEntity,1 ).execute()
        val isFav = checkFav.get()

        if(isFav){
            holder.favRed.setImageResource(R.drawable.ic_favred)
        }else{
            holder.favRed.setImageResource(R.drawable.ic_favborder)
        }

        holder.favRed.setOnClickListener{
            AddToFavourties(holder.restID.toInt(), holder.txtRestName.text as String,
                holder.txtRestRating.text as String,
                holder.txtPrice.text as String,rest.restImage,context,holder.favRed)
        }
    }

    fun getFavourtiesItemCount():Int{
        return getFavourtiesItemCount
    }
}
