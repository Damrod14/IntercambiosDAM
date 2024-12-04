package com.example.appintercambios

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar la base de datos
        database = FirebaseDatabase.getInstance().getReference("users")

        val nameEditText = findViewById<EditText>(R.id.etName)
        val emailEditText = findViewById<EditText>(R.id.etEmailRegister)
        val passwordEditText = findViewById<EditText>(R.id.etPasswordRegister)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val backToLoginText = findViewById<TextView>(R.id.tvBackToLogin)

        // Manejar el clic en "Registrarse"
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val userId = database.push().key ?: "" // Generar un ID único
                val user = User(name, email, password) // Crear un objeto usuario

                // Guardar en la base de datos
                database.child(userId).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                        finish() // Regresa a la actividad anterior
                    } else {
                        Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Manejar el clic en "Inicia sesión aquí"
        backToLoginText.setOnClickListener {
            finish() // Cierra esta actividad y regresa a la anterior
        }
    }
}

data class User(val name: String, val email: String, val password: String)
