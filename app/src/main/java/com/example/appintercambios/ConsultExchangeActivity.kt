package com.example.appintercambios

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ConsultExchangeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consult_exchange)

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvExchangeList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ejemplo de datos
        val exampleData = listOf(
            "Intercambio 1: Descripción breve",
            "Intercambio 2: Detalle adicional",
            "Intercambio 3: Otro intercambio"
        )

        // Configurar adaptador
        val adapter = ExchangeAdapter(exampleData)
        recyclerView.adapter = adapter
    }
}