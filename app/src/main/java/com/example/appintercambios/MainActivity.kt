package com.example.appintercambios

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//importamos firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa Firebase con Realtime Database
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("test")

        // Escribimos
        reference.setValue("Firebase Connected").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseTest", "Data written successfully")
            } else {
                Log.e("FirebaseTest", "Error writing data", task.exception)
            }
        }

        // leemos
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java)
                Log.d("FirebaseTest", "Value read: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseTest", "Error reading data", error.toException())
            }
        })
    }

}