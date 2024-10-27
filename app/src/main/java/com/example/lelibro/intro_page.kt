package com.example.lelibro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class intro_page : AppCompatActivity() {
    private lateinit var toLogin: Button
    private lateinit var toDaftar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro_page)
        window.decorView.systemUiVisibility = 0
        window.navigationBarColor = Color.BLACK
        window.statusBarColor = Color.BLACK

        toLogin = findViewById(R.id.toLogin)
        toDaftar = findViewById(R.id.toDaftar)

        toLogin.setOnClickListener(){
            startActivity(Intent(this, Login::class.java))
        }

        toDaftar.setOnClickListener(){
            startActivity(Intent(this, Daftar::class.java))
        }
    }

}