package com.example.lelibro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class intro_page : AppCompatActivity() {
    private lateinit var toLogin: Button
    private lateinit var toDaftar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro_page)

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