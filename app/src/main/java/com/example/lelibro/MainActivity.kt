package com.example.lelibro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var logoutBtn: ImageButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnAdd: Button
    private lateinit var rv: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private var ListBook: MutableList<Book> = mutableListOf()
    private lateinit var bookdb: DatabaseReference
    private val ADD_BOOK_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = Color.BLACK

        btnAdd = findViewById(R.id.btn_add)
        rv = findViewById(R.id.rv)
        logoutBtn = findViewById(R.id.logout_btn)
        mAuth = FirebaseAuth.getInstance()
        rv.layoutManager = GridLayoutManager(this, 2)
        bookAdapter = BookAdapter(this, ListBook)
        rv.adapter = bookAdapter

        logoutBtn.setOnClickListener() {
            LogoutFun()
        }

        btnAdd.setOnClickListener {
            val intent = Intent(this, AddBook::class.java)
            startActivityForResult(intent, ADD_BOOK_REQUEST_CODE)
        }

        loadBooks()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_BOOK_REQUEST_CODE && resultCode == RESULT_OK) {
            loadBooks()
        }
    }

    private fun loadBooks() {
        val currentUser = mAuth.currentUser
        val userId = currentUser!!.uid
        bookdb = FirebaseDatabase.getInstance("https://le-libro-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("LeLibro")

        bookdb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ListBook.clear()
                for (noteSnapshot in snapshot.children) {
                    val book = noteSnapshot.getValue(Book::class.java)
                    book?.id = noteSnapshot.key
                    if (book != null) {
                        ListBook.add(book)
                    }
                }
                bookAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@MainActivity, "Failed to load Book.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun LogoutFun() {
        mAuth.signOut()
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, intro_page::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
