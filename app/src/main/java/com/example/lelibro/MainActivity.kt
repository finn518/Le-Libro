package com.example.lelibro

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var logoutBtn: ImageButton
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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