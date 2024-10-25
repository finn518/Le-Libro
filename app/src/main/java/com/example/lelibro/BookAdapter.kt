package com.example.lelibro

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BookAdapter(
    private val context: Context,
    private val ListBook: MutableList<Book>,
//    private val dataChangeListener: DataChangeListener? = null
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showJudul: TextView = itemView.findViewById(R.id.showJudul)
        val showPenulis: TextView = itemView.findViewById(R.id.showPenulis)
        val showCover: ImageView = itemView.findViewById(R.id.showCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = ListBook[position]
        holder.itemView.setOnClickListener {
            val intent = Intent(context, viewBook::class.java)
            intent.putExtra("id", currentBook.id)
            intent.putExtra("judul", currentBook.judul)
            intent.putExtra("penulis", currentBook.penulis)
            intent.putExtra("deskripsi", currentBook.deskripsi)
            intent.putExtra("genre", currentBook.genre)
            intent.putExtra("cover", currentBook.cover)
            context.startActivity(intent)
        }

        Glide.with(context).load(currentBook.cover).into(holder.showCover)
        holder.showJudul.text = currentBook.judul
        holder.showPenulis.text = currentBook.penulis
    }

    override fun getItemCount(): Int {
        return ListBook.size
    }
}

//interface DataChangeListener {
//    fun onDataChange()
//}