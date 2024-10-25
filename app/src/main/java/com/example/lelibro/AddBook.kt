package com.example.lelibro

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddBook : AppCompatActivity() {
    private lateinit var back_popup: ImageButton
    private lateinit var add_cover: ImageView
    private lateinit var addJudul: EditText
    private lateinit var addPenulis: EditText
    private lateinit var addDesk: EditText
    private lateinit var addGenre: EditText
    private lateinit var upItem: Button
    private lateinit var bookdb: DatabaseReference
    private var ListBook: MutableList<Book> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        back_popup = findViewById(R.id.back_popup)
        add_cover = findViewById(R.id.upload_cover)
        addJudul = findViewById(R.id.add_judul)
        addPenulis = findViewById(R.id.add_penulis)
        addDesk = findViewById(R.id.add_desk)
        addGenre = findViewById(R.id.add_genre)
        upItem = findViewById(R.id.btn_upload)


        val currentUser = FirebaseAuth.getInstance().currentUser
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
                BookAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddBook, "Failed to load Book.", Toast.LENGTH_SHORT).show()
            }
        })

        add_cover.setOnClickListener {
            selectImage()
        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Take Photo" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 10)
                }
                items[item] == "Choose from Library" -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 20)
                }
                items[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }
}