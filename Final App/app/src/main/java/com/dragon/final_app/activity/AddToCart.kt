package com.dragon.final_app.activity


import android.content.Context

import android.os.AsyncTask

import android.widget.Button
import android.widget.Toast

import androidx.core.content.ContextCompat

import androidx.room.Room

import com.dragon.final_app.R
import com.dragon.final_app.activity.Cartdatabase.FoodDatabase
import com.dragon.final_app.activity.Cartdatabase.FoodEntity


class AddToCart(
    foodId: Int,
    foodName: String,
    foodPrice: String,
    context: Context,
    btnAdd: Button
) {
    val foodEntity = FoodEntity(
        foodId,
        foodName,
        foodPrice
    )

    init {
        val checkItemInCart = DBCartAsyncTask(context, foodEntity, 1).execute()
        val isItemInCart = checkItemInCart.get()
        if (isItemInCart) {
            btnAdd.text = "REMOVE"
            btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
        } else {
            btnAdd.text = "ADD"
            btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }

        if (!DBCartAsyncTask(context, foodEntity, 1).execute().get()) {
            val async = DBCartAsyncTask(context, foodEntity, 2).execute()
            val result = async.get()
            if (result) {
                btnAdd.text = "REMOVE"
                btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
            } else {
                Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
            }
        } else {
            val async = DBCartAsyncTask(context, foodEntity, 3).execute()
            val result = async.get()
            if (result) {
                btnAdd.text = "ADD"
                btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            } else {
                Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

class DBCartAsyncTask(val context: Context, val foodEntity: FoodEntity?, val mode: Int?) :
    AsyncTask<Void, Void, Boolean>() {
    val db = Room.databaseBuilder(context, FoodDatabase::class.java, "cart-db").build()
    override fun doInBackground(vararg params: Void?): Boolean {
        when (mode) {
            1 -> {
                val rest: FoodEntity? = db.foodDao().getCartItemById(foodEntity?.foodId.toString())
                db.close()
                return rest != null
            }
            2 -> {
                if (foodEntity != null) {
                    db.foodDao().insertFood(foodEntity)
                }
                db.close()
                return true
            }
            3 -> {
                if (foodEntity != null) {
                    db.foodDao().deleteFood(foodEntity)
                }
                db.close()
                return true
            }

            4 -> {
                db.foodDao().deleteEverything()
                db.close()
                return true
            }
        }
        return false
    }
}