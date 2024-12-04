package com.example.appintercambios

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EnterExchangeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_exchange)

        val etExchangeKey = findViewById<EditText>(R.id.etExchangeKey)
        val btnEnterExchange = findViewById<Button>(R.id.btnEnterExchange)

        // Simulación de clave válida
        val validExchangeKey = "12345" // Aquí puedes usar datos reales desde un servidor o base de datos.

        btnEnterExchange.setOnClickListener {
            val enteredKey = etExchangeKey.text.toString()

            if (enteredKey.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa una clave.", Toast.LENGTH_SHORT).show()
            } else if (enteredKey == validExchangeKey) {
                Toast.makeText(this, "¡Clave correcta! Entrando al intercambio...", Toast.LENGTH_SHORT).show()
                // Lógica para redirigir a la pantalla del intercambio
                // startActivity(Intent(this, ExchangeDetailsActivity::class.java))
            } else {
                Toast.makeText(this, "Clave incorrecta. Intenta nuevamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
