package com.dragon.final_app.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragon.final_app.R
import com.dragon.final_app.activity.model.CartItems

class CartRecyclerAdapter(
    val context: Context,
    val itemList: ArrayList<CartItems>
) : RecyclerView.Adapter<CartRecyclerAdapter.cartViewHolder>() {

    class cartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFoodName: TextView = view.findViewById(R.id.txtCartFoodName)
        val txtFoodPrice: TextView = view.findViewById(R.id.txtCartFoodPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_cart_single_row, parent, false)
        return cartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        try {

            val food = itemList[position]
            holder.txtFoodName.text = food.itemName
            holder.txtFoodPrice.text = "Rs. " + food.itemPrice
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Out of Bounds $e")
        }
    }
}