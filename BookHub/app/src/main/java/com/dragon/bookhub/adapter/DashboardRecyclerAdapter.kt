package com.dragon.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dragon.bookhub.R
import com.dragon.bookhub.activity.DescriptionActivity
import com.dragon.bookhub.model.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.view.*
import org.w3c.dom.Text

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){

    class DashboardViewHolder(view:View):RecyclerView.ViewHolder(view){
        val textBookName:TextView = view.findViewById(R.id.txtBookName)
        val textBookAuthor:TextView = view.findViewById(R.id.txtBookAuthor)
        val textBookPrice:TextView = view.findViewById(R.id.txtBookPrice)
        val textBookRating:TextView = view.findViewById(R.id.txtBookRating)
        val imgBookImage:ImageView = view.findViewById(R.id.imgBookImage )
        val llContent:LinearLayout = view.findViewById(R.id.llContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        try {
            var book = itemList[position]
            holder.textBookName.text = book.bookName
            holder.textBookAuthor.text = book.bookAuthor
            holder.textBookPrice.text = book.bookCost
            holder.textBookRating.text = book.bookRating
            //     holder.imgBookImage.setImageResource(book.bookImage)
            Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)
            holder.llContent.setOnClickListener{
                val intent = Intent(context , DescriptionActivity::class.java)
                intent.putExtra("book_id",book.bookId)
                context.startActivity(intent)
            }
        }catch (e:ArrayIndexOutOfBoundsException){
            println("Out of Bounds $e")
        }

    }
}