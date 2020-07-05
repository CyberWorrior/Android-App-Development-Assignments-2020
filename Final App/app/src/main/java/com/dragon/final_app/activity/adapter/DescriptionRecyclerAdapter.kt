package com.dragon.final_app.activity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dragon.final_app.R
import com.dragon.final_app.activity.AddToCart
import com.dragon.final_app.activity.CartPageActivity
import com.dragon.final_app.activity.Cartdatabase.FoodEntity
import com.dragon.final_app.activity.DBCartAsyncTask
import com.dragon.final_app.activity.model.Food


class DescriptionRecyclerAdapter(
    val context: Context,
    val itemList: ArrayList<Food>,
    val btnProceedToCart: Button,
    val restId: String,
    val title: String
) : RecyclerView.Adapter<DescriptionRecyclerAdapter.DescriptionViewHolder>() {

    var itemsSelectedCount: Int = 0
    var itemsSelectedId = arrayListOf<String>()


    class DescriptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodName)
        val txtFooPrice: TextView = view.findViewById(R.id.txtFoodPrice)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)
        val txtSerialNumber: TextView = view.findViewById(R.id.txtSerialNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_description_single_row, parent, false)
        return DescriptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        try {
            var food = itemList[position]
            holder.txtFoodName.text = food.foodName
            holder.txtFooPrice.text = "Rs. " + food.FoodPrice
            holder.txtSerialNumber.text = (position + 1).toString()
            holder.btnAdd.tag = food.foodId

            val foodEntity = FoodEntity(
                food.foodId,
                food.foodName,
                food.FoodPrice
            )

            val inCart = DBCartAsyncTask(context, foodEntity, 1).execute()
            val isInCart = inCart.get()

            if (isInCart) {
                holder.btnAdd.text = "REMOVE"
                holder.btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
            } else {
                holder.btnAdd.text = "ADD"
                holder.btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            }

            btnProceedToCart.setOnClickListener {
                val intent = Intent(context, CartPageActivity::class.java)
                intent.putExtra("restaurantId", restId)
                intent.putExtra("restName", title)
                intent.putExtra("selectedItemsId", itemsSelectedId)
                context.startActivity(intent)
            }

            holder.btnAdd.setOnClickListener {
                if (holder.btnAdd.text.toString().equals("REMOVE")) {
                    itemsSelectedCount--
                    itemsSelectedId.remove(holder.btnAdd.getTag().toString())
                } else {
                    itemsSelectedCount++
                    itemsSelectedId.add(holder.btnAdd.getTag().toString())
                }
                println("itemSelectedIds $itemsSelectedId")
                notifyDataSetChanged()
                AddToCart(food.foodId, food.foodName, food.FoodPrice, context, holder.btnAdd)
            }

            if (itemsSelectedCount > 0) {
                btnProceedToCart.visibility = View.VISIBLE
                println("itemsSelectedCount $itemsSelectedCount")
            } else {
                btnProceedToCart.visibility = View.GONE
                println("itemsSelectedCount $itemsSelectedCount")
            }


        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Out of Bounds $e")
        }

    }

    fun setSelectedItemCount(value: Int) {
        itemsSelectedCount = value
    }

    fun getSelectedItemCount(): Int {
        return itemsSelectedCount
    }
}