package com.example.lelibro

import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var logoutBtn: ImageButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnAdd: Button
    private lateinit var rv: RecyclerView
    private lateinit var dark_layout: ImageView
    private lateinit var popup_add: ScrollView
    private lateinit var back_popup: ImageButton
    private lateinit var add_cover: ImageView
    private lateinit var addJudul: EditText
    private lateinit var addPenulis: EditText
    private lateinit var addDesk: EditText
    private lateinit var addGenre: EditText
    private lateinit var upItem: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd = findViewById(R.id.btn_add)
        rv = findViewById(R.id.rv)
        dark_layout = findViewById(R.id.dark)
        popup_add = findViewById(R.id.popup_add)
        back_popup = findViewById(R.id.back_popup)
        add_cover = findViewById(R.id.upload_cover)
        addJudul = findViewById(R.id.add_judul)
        addPenulis = findViewById(R.id.add_penulis)
        addDesk = findViewById(R.id.add_desk)
        addGenre = findViewById(R.id.add_genre)
        upItem = findViewById(R.id.btn_upload)

        logoutBtn = findViewById(R.id.logout_btn)
        mAuth = FirebaseAuth.getInstance()
        logoutBtn.setOnClickListener(){
            LogoutFun()
        }

    }

    private fun LogoutFun(){
        mAuth!!.signOut()
        startActivity(Intent(this, intro_page::class.java))
    }
}