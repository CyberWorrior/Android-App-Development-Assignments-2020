package com.dragon.login_register.activity.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dragon.login_register.R
import com.dragon.login_register.activity.AddToFavourties
import com.dragon.login_register.activity.database.RestDatabase
import com.dragon.login_register.activity.database.RestEntity
import com.dragon.login_register.activity.model.Rest
import com.squareup.picasso.Picasso


class HomeRecyclerAdapter(val context:Context,val itemList:ArrayList<Rest>):RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtRestName:TextView = view.findViewById(R.id.txtRestName)
        val txtRestRating:TextView = view.findViewById(R.id.txtRating)
        val txtRestPrice:TextView = view.findViewById(R.id.txtPrice)
        val imgRestImage:ImageView = view.findViewById(R.id.imgRestImage)
        val favBorder:ImageView = view.findViewById(R.id.imgFavborder)
        val llContent:LinearLayout = view.findViewById(R.id.llContent)
        var restId:String= "100"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)

        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        try{
            var rest = itemList[position]
            holder.restId = rest.restId
            holder.txtRestName.text = rest.restName
            holder.txtRestRating.text = rest.restRating
            holder.txtRestPrice.text = rest.Price
            Picasso.get().load(rest.restImage).error(R.drawable.ic_launcher_background).into(holder.imgRestImage)

            val restEntity = RestEntity(
                holder.restId?.toInt(),
                holder.txtRestName.text.toString(),
                holder.txtRestRating.text.toString(),
                holder.txtRestPrice.text.toString(),
                rest.restImage
            )

            val checkFav = DBAsyncTask(context,restEntity,1 ).execute()
            val isFav = checkFav.get()

            if(isFav){
                holder.favBorder.setImageResource(R.drawable.ic_favred)
            }else{
                holder.favBorder.setImageResource(R.drawable.ic_favborder)
            }

            holder.favBorder.setOnClickListener{
                AddToFavourties(holder.restId.toInt(), holder.txtRestName.text as String,
                    holder.txtRestRating.text as String,
                    holder.txtRestPrice.text as String,rest.restImage,context,holder.favBorder)
            }
        }catch (e:ArrayIndexOutOfBoundsException){
            println("Out of Bounds $e")
        }
    }

}

class DBAsyncTask(val context: Context, val restEntity: RestEntity, val mode:Int):
    AsyncTask<Void, Void, Boolean>(){
    val db = Room.databaseBuilder(context, RestDatabase::class.java,"rests-db").build()
    override fun doInBackground(vararg params: Void?): Boolean {
        when(mode){
            1 ->{
                val rest: RestEntity?= db.restDao().getRestById(restEntity.rest_id.toString())
                db.close()
                return rest!=null
            }
            2 ->{
                db.restDao().insertFood(restEntity)
                db.close()
                return true
            }
            3 ->{
                db.restDao().deleteFood(restEntity)
                db.close()
                return true
            }
        }
        return false
    }


}