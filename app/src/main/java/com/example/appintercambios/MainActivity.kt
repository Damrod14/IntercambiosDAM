package com.example.appintercambios

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().getReference("users")

        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerTextView = findViewById<TextView>(R.id.tvRegister)

        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                database.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (userSnapshot in snapshot.children) {
                                    val userPassword = userSnapshot.child("password").getValue(String::class.java)
                                    if (userPassword == password) {
                                        val userName = userSnapshot.child("name").getValue(String::class.java)
                                        val userEmail = userSnapshot.child("email").getValue(String::class.java)

                                        // Guardar sesión en SharedPreferences
                                        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                                        val editor = sharedPref.edit()
                                        editor.putString("user_name", userName)
                                        editor.putString("user_email", userEmail)
                                        editor.apply()

                                        Toast.makeText(this@MainActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@MainActivity, ThirdActivity::class.java))
                                        finish()
                                        return
                                    }
                                }
                                Toast.makeText(this@MainActivity, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@MainActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("FirebaseError", error.message)
                            Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
