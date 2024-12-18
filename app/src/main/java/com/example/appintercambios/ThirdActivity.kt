package com.example.appintercambios

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", null)
        val userEmail = sharedPref.getString("user_email", null)

        if (userEmail == null) {
            // Redirigir a inicio de sesión si no hay sesión
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }


        // Configurar botones
        findViewById<Button>(R.id.btnCreateExchange).setOnClickListener {
            startActivity(Intent(this, CreateExchangeActivity::class.java))
        }

        findViewById<Button>(R.id.btnEditDeleteExchange).setOnClickListener {
            startActivity(Intent(this, EditDeleteExchangeActivity::class.java))
        }

    }
}
