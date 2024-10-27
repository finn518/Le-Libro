package com.example.lelibro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.statusBarColor = Color.BLACK

        mAuth = FirebaseAuth.getInstance()

        // Hide Status Bar using WindowInsetsController
        val decorView = window.decorView
        val controller = ViewCompat.getWindowInsetsController(decorView)
        controller?.hide(WindowInsetsCompat.Type.statusBars())
        controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Hide ActionBar
        supportActionBar?.hide()

        // Timer to move to Intro Page
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreenActivity, intro_page::class.java))
//            CheckLogin()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // Optional transition animation
            finish()
        }, 3000)
    }

//    private fun CheckLogin(){
//        // Cek apakah ada pengguna yang sudah login
//        val currentUser= mAuth.currentUser
//        if (currentUser != null) {
//            // Jika sudah login, langsung ke MainActivity
//            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
//        } else {
//            // Jika belum login, arahkan ke intro_page
//            startActivity(Intent(this@SplashScreenActivity, intro_page::class.java))
//        }
//        finish()
//    }
}
