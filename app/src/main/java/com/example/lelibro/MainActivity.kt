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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd = findViewById(R.id.btn_add)
        rv = findViewById(R.id.rv)

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