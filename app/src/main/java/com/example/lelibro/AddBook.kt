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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException

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
    private val DEFAULT_IMAGE_URL = "https://drive.google.com/file/d/102OuLDql0GymVcHPJpQwxgK05aUD1Puu/view?usp=sharing"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        window.statusBarColor = Color.BLACK

        back_popup = findViewById(R.id.back_add)
        add_cover = findViewById(R.id.upload_cover)
        addJudul = findViewById(R.id.add_judul)
        addPenulis = findViewById(R.id.add_penulis)
        addDesk = findViewById(R.id.add_desk)
        addGenre = findViewById(R.id.add_genre)
        upItem = findViewById(R.id.btn_upload)

        bookdb = FirebaseDatabase.getInstance("https://le-libro-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("LeLibro")

        back_popup.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        upItem.setOnClickListener {
            if (!validateForm()) {
                return@setOnClickListener
            }
            val bookId = bookdb.push().key ?: return@setOnClickListener
            uploadImg(bookId) { imageUrl ->
                saveBookData(bookId, imageUrl)
            }
        }

        add_cover.setOnClickListener {
            selectImage()
        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Ambil Foto", "Pilih dari galeri", "Batal")
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Ambil Foto" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 10)
                }
                items[item] == "Pilih dari galeri" -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 20)
                }
                items[item] == "Batal" -> dialog.dismiss()
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
                    add_cover.post {
                        add_cover.setImageBitmap(bitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            thread.start()
        }

        if (requestCode == 10 && resultCode == RESULT_OK) {
            val extras = data?.extras
            val thread = Thread {
                val bitmap = extras?.get("data") as Bitmap
                add_cover.post {
                    add_cover.setImageBitmap(bitmap)
                }
            }
            thread.start()
        }
    }

    private fun uploadImg(bookId: String, onComplete: (String) -> Unit) {
        if (add_cover.drawable == null) {
            onComplete(DEFAULT_IMAGE_URL)
            return
        }
        add_cover.isDrawingCacheEnabled = true
        add_cover.buildDrawingCache()
        val drawable = add_cover.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images/$bookId.jpeg")
            val uploadTask = storageRef.putBytes(data)
            uploadTask.addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
                onComplete(DEFAULT_IMAGE_URL)
            }.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result.toString()
                        onComplete(downloadUrl)
                    } else {
                        Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                        onComplete(DEFAULT_IMAGE_URL)
                    }
                }
            }
        } else {
            onComplete(DEFAULT_IMAGE_URL)
        }
    }

    private fun saveBookData(bookId: String, imageUrl: String) {
        val judul = addJudul.text.toString()
        val penulis = addPenulis.text.toString()
        val desc = addDesk.text.toString()
        val genre = addGenre.text.toString()

        val books = Book(
            id = bookId,
            judul = judul,
            penulis = penulis,
            deskripsi = desc,
            genre = genre,
            cover = imageUrl
        )
        bookdb.child(bookId).setValue(books).addOnSuccessListener {
            Toast.makeText(this@AddBook, "Buku berhasil ditambah", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@AddBook, "Gagal menambahkan buku", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(): Boolean {
        var result = true
        if (TextUtils.isEmpty(addJudul.text.toString())) {
            addJudul.error = "Wajib diisi"
            result = false
        } else {
            addJudul.error = null
        }
        if (TextUtils.isEmpty(addPenulis.text.toString())) {
            addPenulis.error = "Wajib diisi"
            result = false
        } else {
            addPenulis.error = null
        }
        if (TextUtils.isEmpty(addDesk.text.toString())) {
            addDesk.error = "Wajib diisi"
            result = false
        } else {
            addDesk.error = null
        }
        if (TextUtils.isEmpty(addGenre.text.toString())) {
            addGenre.error = "Wajib diisi"
            result = false
        } else {
            addGenre.error = null
        }
        return result
    }
}
