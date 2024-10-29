package com.example.lelibro

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException

class Edit : AppCompatActivity() {
    private lateinit var back_update: ImageButton
    private lateinit var edit_cover: ImageView
    private lateinit var edit_judul: EditText
    private lateinit var edit_penulis: EditText
    private lateinit var edit_desk: EditText
    private lateinit var edit_genre: EditText
    private lateinit var btn_update: Button
    private lateinit var bookdb: DatabaseReference
    private var bookId: String? = null
    private var currentCoverUrl: String? = null
    private val DEFAULT_IMAGE_URL = "https://drive.google.com/file/d/102OuLDql0GymVcHPJpQwxgK05aUD1Puu/view?usp=sharing"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        window.statusBarColor = Color.BLACK

        back_update = findViewById(R.id.back_edit)
        edit_cover = findViewById(R.id.edit_cover)
        edit_judul = findViewById(R.id.edit_judul)
        edit_penulis = findViewById(R.id.edit_penulis)
        edit_desk = findViewById(R.id.edit_desk)
        edit_genre = findViewById(R.id.edit_genre)
        btn_update = findViewById(R.id.btn_update)

        bookdb = FirebaseDatabase.getInstance("https://le-libro-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("LeLibro")

        val intent = intent
        bookId = intent.getStringExtra("id")
        edit_judul.setText(intent.getStringExtra("title"))
        edit_penulis.setText(intent.getStringExtra("author"))
        edit_desk.setText(intent.getStringExtra("desk"))
        edit_genre.setText(intent.getStringExtra("genre"))
        currentCoverUrl = intent.getStringExtra("img")
        Glide.with(this).load(currentCoverUrl).into(edit_cover)

        back_update.setOnClickListener {
//            startActivity(Intent(this, viewBook::class.java))
            finish()
        }

        edit_cover.setOnClickListener {
            selectImage()
        }

        btn_update.setOnClickListener {
            if (validateForm()) {
                uploadImg(bookId!!) { imageUrl ->
                    updateBookData(imageUrl)
                }
            }
        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Ambil Foto", "Pilih dari galeri", "Batal")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setItems(items) { dialog, item ->
            when (items[item]) {
                "Ambil Foto" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 10)
                }
                "Pilih dari galeri" -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 20)
                }
                "Batal" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            val path: Uri? = data.data
            val thread = Thread {
                try {
                    val inputStream = contentResolver.openInputStream(path!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    edit_cover.post {
                        edit_cover.setImageBitmap(bitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            thread.start()
        } else if (requestCode == 10 && resultCode == RESULT_OK) {
            val extras = data?.extras
            val thread = Thread {
                val bitmap = extras?.get("data") as Bitmap
                edit_cover.post {
                    edit_cover.setImageBitmap(bitmap)
                }
            }
            thread.start()
        }
    }

    private fun uploadImg(bookId: String, onComplete: (String) -> Unit) {
        if (edit_cover.drawable == null) {
            onComplete(currentCoverUrl ?: DEFAULT_IMAGE_URL)
            return
        }
        edit_cover.isDrawingCacheEnabled = true
        edit_cover.buildDrawingCache()
        val drawable = edit_cover.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images/$bookId.jpeg")
            val uploadTask = storageRef.putBytes(data)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result.toString()
                        onComplete(downloadUrl)
                    } else {
                        Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                        onComplete(currentCoverUrl ?: DEFAULT_IMAGE_URL)
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                onComplete(currentCoverUrl ?: DEFAULT_IMAGE_URL)
            }
        } else {
            onComplete(currentCoverUrl ?: DEFAULT_IMAGE_URL)
        }
    }

    private fun updateBookData(imageUrl: String) {
        val judul = edit_judul.text.toString()
        val penulis = edit_penulis.text.toString()
        val desc = edit_desk.text.toString()
        val genre = edit_genre.text.toString()

        val updatedBook = Book(
            id = bookId,
            judul = judul,
            penulis = penulis,
            deskripsi = desc,
            genre = genre,
            cover = imageUrl
        )

        bookdb.child(bookId!!).setValue(updatedBook).addOnSuccessListener {
            Toast.makeText(this, "Buku berhasil diupdate", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengupdate buku", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(): Boolean {
        var result = true
        if (TextUtils.isEmpty(edit_judul.text.toString())) {
            edit_judul.error = "Wajib diisi"
            result = false
        }
        if (TextUtils.isEmpty(edit_penulis.text.toString())) {
            edit_penulis.error = "Wajib diisi"
            result = false
        }
        if (TextUtils.isEmpty(edit_desk.text.toString())) {
            edit_desk.error = "Wajib diisi"
            result = false
        }
        if (TextUtils.isEmpty(edit_genre.text.toString())) {
            edit_genre.error = "Wajib diisi"
            result = false
        }
        return result
    }
}
