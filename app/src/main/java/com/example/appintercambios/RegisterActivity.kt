package com.example.appintercambios
//esta es la interfaz 2
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        findViewById<TextView>(R.id.tvBackToLogin).setOnClickListener {
            finish() // Vuelve a la actividad anterior
        }
    }


}
