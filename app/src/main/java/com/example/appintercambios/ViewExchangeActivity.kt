package com.example.appintercambios

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewExchangesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_exchanges)

        val rvExchanges = findViewById<RecyclerView>(R.id.rvExchanges)

        // Datos simulados
        val exchanges = listOf(
            Exchange("Intercambio Navideño", "2024-12-15", 10),
            Exchange("Cumpleaños Juan", "2024-01-20", 8),
            Exchange("Amigo Secreto", "2024-02-14", 12)
        )

        // Configurar el RecyclerView
        rvExchanges.layoutManager = LinearLayoutManager(this)
        //rvExchanges.adapter = ExchangeAdapter(exchanges)
    }
}
