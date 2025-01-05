package com.example.appintercambios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewExchangesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_exchanges)

        val rvExchanges = findViewById<RecyclerView>(R.id.rvExchanges)



        // Configurar el RecyclerView
        rvExchanges.layoutManager = LinearLayoutManager(this)
        //rvExchanges.adapter = ExchangeAdapter(exchanges)
        val btnGoToEnterKey: Button = findViewById(R.id.btnGoToEnterKey)
        btnGoToEnterKey.setOnClickListener {
            val intent = Intent(this, ConsultExchangeActivity::class.java)
            startActivity(intent)
        }



    }
}
