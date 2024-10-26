package com.example.lelibro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class viewBook : AppCompatActivity() {
    private var linkImg: String? = null
    private var key: String? = null
    private lateinit var detailImg: ImageView
    private lateinit var  detailTitle: TextView
    private lateinit var detailAuthor: TextView
    private lateinit var detailGenre: TextView
    private lateinit var detailDesc: TextView
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var downloadBtn: ImageButton
    private lateinit var backBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_book)

        detailImg = findViewById(R.id.detailImage)
        detailTitle = findViewById(R.id.detailTitle)
        detailAuthor = findViewById(R.id.detailAuthor)
        detailGenre = findViewById(R.id.detailGenre)
        detailDesc = findViewById(R.id.detailDesc)
        editBtn = findViewById(R.id.editBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        downloadBtn = findViewById(R.id.downloadBtn)
        backBtn = findViewById(R.id.backToMain)


        val bundleBook: Bundle? = intent.extras

        if (bundleBook != null){
            detailTitle?.text = bundleBook.getString("judul")
            detailAuthor?.text = bundleBook.getString("penulis")
            detailGenre?.text = bundleBook.getString("genre")
            detailDesc?.text = bundleBook.getString("deskripsi")
            key = bundleBook.getString("id")
            linkImg = bundleBook.getString("cover")
            if (detailImg != null && linkImg != null){
                Glide.with(this).load(linkImg).into(detailImg!!)
            }
        }

        editBtn.setOnClickListener(){
            SendToEdit()
        }

        deleteBtn.setOnClickListener(){
            DeleteBook()
        }

        backBtn.setOnClickListener(){
            finish()
        }

    }

    private fun SendToEdit(){
        val intent = Intent(this, Edit::class.java)
            .putExtra("title", detailTitle.text.toString())
            .putExtra("author", detailAuthor.text.toString())
            .putExtra("genre", detailGenre.text.toString())
            .putExtra("id", key)
            .putExtra("img", linkImg)
        startActivity(intent)
    }

    private fun DeleteBook() {
        var ref: DatabaseReference = FirebaseDatabase.getInstance("https://le-libro-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("LeLibro")
        Log.d("ref", ref.child(key!!).toString())
        var storage: FirebaseStorage = FirebaseStorage.getInstance()

        if (linkImg != null && key != null) {
            try {
                val storageRef: StorageReference = storage.getReferenceFromUrl(linkImg!!)
                Log.d("link", linkImg.toString())
                storageRef.delete().addOnSuccessListener {
                    ref.child(key!!).removeValue().addOnSuccessListener {
                        Toast.makeText(this, "Berhasil Dihapus", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Gagal Menghapus dari Database", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception ->
                    Log.e("storage_error", "Failed to delete from Storage", exception)
                    Toast.makeText(this, "Gagal menghapus dari storage", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("delete_error", "An error occurred", e)
                Toast.makeText(this, "Terjadi kesalahan saat menghapus file dari storage", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "URL atau kunci tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }
}