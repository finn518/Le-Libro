package com.example.lelibro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddBook : AppCompatActivity() {
    private lateinit var back_popup: ImageButton
    private lateinit var add_cover: ImageView
    private lateinit var addJudul: EditText
    private lateinit var addPenulis: EditText
    private lateinit var addDesk: EditText
    private lateinit var addGenre: EditText
    private lateinit var upItem: Button

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

    }
}