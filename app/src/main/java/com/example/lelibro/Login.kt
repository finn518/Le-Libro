package com.example.lelibro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {
    private lateinit var backToIntro : ImageButton
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText
    private lateinit var loginBtn: Button
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        window.decorView.systemUiVisibility = 0
        window.navigationBarColor = Color.BLACK
        window.statusBarColor = Color.BLACK

        backToIntro = findViewById(R.id.backToIntro)
        emailLogin = findViewById(R.id.masukEmail)
        passwordLogin = findViewById(R.id.masukPassword)
        loginBtn = findViewById(R.id.loginBtn)
        mAuth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener(){
            LoginFun(emailLogin!!.text.toString(), passwordLogin!!.text.toString())
        }

        backToIntro.setOnClickListener(){
            finish()
        }

    }

    private fun validate(): Boolean{
        var result = true
        if (TextUtils.isEmpty(emailLogin!!.text.toString())){
            emailLogin!!.error = "Email Harus diisi"
            result = false
        } else {
            emailLogin!!.error = null
        }

        if (TextUtils.isEmpty(passwordLogin!!.text.toString())){
            passwordLogin!!.error = "Password Harus diisi"
            result = false
        } else {
            passwordLogin!!.error = null
        }
        return result
    }

    private fun updateUI(user: FirebaseUser?){
        if (user != null){
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, "Daftar Akun Terlebih Dahulu", Toast.LENGTH_LONG).show()
        }
    }

    private fun LoginFun(email: String?, password: String?){
        if (!validate()){
            return
        }
        mAuth!!.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(this){task ->
            if (task.isSuccessful){
                val user = mAuth.currentUser
                updateUI(user)
            }else {
                Toast.makeText(this, "Gagal Login", Toast.LENGTH_LONG).show()
                updateUI(null)
            }
        }
    }
}