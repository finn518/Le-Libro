package com.example.lelibro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val context: Context,
    private val ListBook: MutableList<Book>,
    private val dataChangeListener: DataChangeListener? = null
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return ListBook.size
    }

    companion object {
        fun notifyDataSetChanged() {
            TODO("Not yet implemented")
        }
    }
}

interface DataChangeListener {
    fun onDataChange()
}