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
import org.w3c.dom.Text

class Daftar : AppCompatActivity() {
    private lateinit var backToIntro : ImageButton
    private lateinit var emailDaftar: EditText
    private lateinit var passwordDaftar: EditText
    private lateinit var konfirmasiPassword: EditText
    private lateinit var daftarBtn : Button
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daftar)
        window.decorView.systemUiVisibility = 0
        window.navigationBarColor = Color.BLACK
        window.statusBarColor = Color.BLACK

        backToIntro = findViewById(R.id.backToIntro)
        emailDaftar = findViewById(R.id.daftarEmail)
        passwordDaftar = findViewById(R.id.daftarPassword)
        konfirmasiPassword = findViewById(R.id.daftarPasswordKonfirmasi)
        daftarBtn = findViewById(R.id.daftarBtn)
        mAuth = FirebaseAuth.getInstance()

        daftarBtn.setOnClickListener(){
            DaftarBaru(emailDaftar!!.text.toString(), passwordDaftar!!.text.toString())
        }
        backToIntro.setOnClickListener(){
            finish()
        }
    }

    private fun validate(): Boolean{
        var result = true
        if (TextUtils.isEmpty(emailDaftar!!.text.toString())){
            emailDaftar!!.error = "Email Harus diisi"
            result = false
        } else {
            emailDaftar!!.error = null
        }

        if (TextUtils.isEmpty(passwordDaftar!!.text.toString())){
            passwordDaftar!!.error = "Password Harus diisi"
            result = false
        } else {
            passwordDaftar!!.error = null
        }

        if (TextUtils.isEmpty(konfirmasiPassword!!.text.toString())){
            konfirmasiPassword!!.error = "Konfirmasi Password Harus diisi"
            result = false
        }   else {
            konfirmasiPassword!!.error = null
        }

        if (passwordDaftar.text.toString() != konfirmasiPassword.text.toString()){
            konfirmasiPassword.error = "Password Tidak Sama"
            result = false
        }

        return result
    }

    private fun updateUI(user: FirebaseUser?){
        if (user != null){
            startActivity(Intent(this, intro_page::class.java))
        } else {
            Toast.makeText(this, "Password Wajib 8 Karakter", Toast.LENGTH_LONG).show()
        }
    }

    private fun DaftarBaru(email: String?, password: String?){
        if (!validate()){
            return
        }
        mAuth!!.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                val user = mAuth!!.currentUser
                updateUI(user)
            } else {
                Toast.makeText(this, "Gagal Buat Akun", Toast.LENGTH_LONG).show()
                updateUI(null)
            }
        }
    }
}