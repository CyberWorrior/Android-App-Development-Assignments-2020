package com.dragon.final_app.activity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragon.final_app.R
import com.dragon.final_app.activity.AddToFavourties
import com.dragon.final_app.activity.RestaurantMenuActivity
import com.dragon.final_app.activity.database.RestEntity
import com.squareup.picasso.Picasso



class FavouriteRecyclerAdapter(val context: Context, val itemList: List<RestEntity>) :
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtRestName: TextView = view.findViewById(R.id.txtFavRestName)
        val txtRestRating: TextView = view.findViewById(R.id.txtFavRating)
        val txtPrice: TextView = view.findViewById(R.id.txtFavPrice)
        val imgFavImage: ImageView = view.findViewById(R.id.imgFavRestImage)
        val favRed: ImageView = view.findViewById(R.id.imgFavFavMark)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        var restID = "100"


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourite_single, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        var rest = itemList[position]
        holder.restID = rest.rest_id.toString()
        holder.txtRestName.text = rest.restName
        holder.txtRestRating.text = rest.rating
        holder.txtPrice.text = rest.price
        Picasso.get().load(rest.restImage).error(R.drawable.ic_launcher_background)
            .into(holder.imgFavImage)
        val restEntity = RestEntity(
            holder.restID.toInt(),
            holder.txtRestName.text.toString(),
            holder.txtRestRating.text.toString(),
            holder.txtPrice.text.toString(),
            rest.restImage
        )

        val checkFav = DBAsyncTask(context, restEntity, 1).execute()
        val isFav = checkFav.get()

        if (isFav) {
            holder.favRed.setImageResource(R.drawable.ic_favred)
        } else {
            holder.favRed.setImageResource(R.drawable.ic_favborder)
        }

        holder.favRed.setOnClickListener {
            notifyDataSetChanged()
            AddToFavourties(
                holder.restID.toInt(), holder.txtRestName.text as String,
                holder.txtRestRating.text as String,
                holder.txtPrice.text as String, rest.restImage, context, holder.favRed
            )
        }

        holder.llContent.setOnClickListener {
            val intent = Intent(context, RestaurantMenuActivity::class.java)
            intent.putExtra("restId", rest.rest_id.toString())
            intent.putExtra("restName", rest.restName)
            context.startActivity(intent)
        }


    }
}
