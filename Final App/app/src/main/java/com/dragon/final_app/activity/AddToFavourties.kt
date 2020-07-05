package com.dragon.final_app.activity

import android.content.Context
import android.os.AsyncTask
import android.widget.ImageView
import android.widget.Toast
import androidx.room.Room
import com.dragon.final_app.R
import com.dragon.final_app.activity.database.RestDatabase
import com.dragon.final_app.activity.database.RestEntity

class AddToFavourties(
    restId: Int,
    restName: String,
    restRating: String,
    restPrice: String,
    restImage: String,
    context: Context,
    favBorder: ImageView
) {
    val restEntity = RestEntity(
        restId,
        restName,
        restRating,
        restPrice,
        restImage
    )

    init {
        println("RestEntity is $restEntity")
        val checkFav = DBAsyncTask(context, restEntity, 1).execute()
        val isFav = checkFav.get()

        if (isFav) {
            favBorder.setImageResource(R.drawable.ic_favred)
        } else {
            favBorder.setImageResource(R.drawable.ic_favborder)
        }

        if (!DBAsyncTask(context, restEntity, 1).execute().get()) {
            val async = DBAsyncTask(context, restEntity, 2).execute()
            val result = async.get()
            if (result) {
                favBorder.setImageResource(R.drawable.ic_favred)
            } else {
                Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
            }
        } else {
            val async = DBAsyncTask(context, restEntity, 3).execute()
            val result = async.get()
            if (result) {
                favBorder.setImageResource(R.drawable.ic_favborder)
            } else {
                Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }


}

class DBAsyncTask(val context: Context, val restEntity: RestEntity?, val mode: Int) :
    AsyncTask<Void, Void, Boolean>() {
    val db = Room.databaseBuilder(context, RestDatabase::class.java, "rests-db").build()
    override fun doInBackground(vararg params: Void?): Boolean? {
        when (mode) {
            1 -> {
                val rest: RestEntity? = db.restDao().getRestById(restEntity?.rest_id.toString())
                db.close()
                return rest != null
            }
            2 -> {
                if (restEntity != null) {
                    db.restDao().insertFood(restEntity)
                }
                db.close()
                return true
            }
            3 -> {
                if (restEntity != null) {
                    db.restDao().deleteFood(restEntity)
                }
                db.close()
                return true
            }
        }
        return false
    }

}

