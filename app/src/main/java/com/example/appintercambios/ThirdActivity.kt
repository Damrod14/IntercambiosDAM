package com.example.appintercambios

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        findViewById<Button>(R.id.btnCreateExchange).setOnClickListener {
            val intent = Intent(this, CreateExchangeActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnEditDeleteExchange).setOnClickListener {
            val intent = Intent(this, EditDeleteExchangeActivity::class.java)
            startActivity(intent)
        }
    }
}
