package com.example.smb1.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.smb1.R
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
    }

    fun Login(view: android.view.View) {
        val username = findViewById<EditText>(R.id.editTextUsername).text.toString()
        val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
        mAuth.signInWithEmailAndPassword(
            username,
            password
        ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("user", mAuth.currentUser)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Log-in failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun Register(view: android.view.View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}