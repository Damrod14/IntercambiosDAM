package com.example.appintercambios

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ConsultExchangeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var layoutContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consult_exchanges)

        // Inicializar el contenedor para los datos
        layoutContainer = findViewById(R.id.layoutContainer)

        // Configurar Firebase
        database = FirebaseDatabase.getInstance().getReference("exchanges")

        // Obtener email del usuario autenticado
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", null)

        if (userEmail == null) {
            Toast.makeText(this, "Debe iniciar sesión para ver sus intercambios", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Consultar intercambios creados por el usuario actual
        queryUserExchanges(userEmail)
    }

    private fun queryUserExchanges(userEmail: String) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                layoutContainer.removeAllViews() // Limpiar la vista antes de mostrar nuevos datos
                var foundExchanges = false

                if (snapshot.exists()) {
                    for (exchangeSnapshot in snapshot.children) {
                        val creatorEmail = exchangeSnapshot.child("creatorEmail").getValue(String::class.java)
                        val emails = exchangeSnapshot.child("emails").getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()

                        // Verificar si el correo del usuario es el creador o está en la lista de correos
                        if (creatorEmail == userEmail || emails.contains(userEmail)) {
                            foundExchanges = true
                            val uniqueKey = exchangeSnapshot.child("uniqueKey").getValue(String::class.java) ?: "Sin clave"
                            val theme = exchangeSnapshot.child("theme").getValue(String::class.java) ?: "Sin tema"
                            val exchangeDate = exchangeSnapshot.child("exchangeDate").getValue(String::class.java) ?: "Sin fecha"

                            // Crear un TextView para mostrar los datos
                            val exchangeView = TextView(this@ConsultExchangeActivity).apply {
                                text = "Clave: $uniqueKey\nTema: $theme\nFecha: $exchangeDate"
                                textSize = 16f
                                setPadding(16, 16, 16, 16)
                            }

                            // Agregar el TextView al contenedor
                            layoutContainer.addView(exchangeView)
                        }
                    }

                    if (!foundExchanges) {
                        val noDataView = TextView(this@ConsultExchangeActivity).apply {
                            text = "No se encontraron intercambios relacionados con su correo"
                            textSize = 16f
                            setPadding(16, 16, 16, 16)
                        }
                        layoutContainer.addView(noDataView)
                    }
                } else {
                    val noDataView = TextView(this@ConsultExchangeActivity).apply {
                        text = "No se encontraron intercambios"
                        textSize = 16f
                        setPadding(16, 16, 16, 16)
                    }
                    layoutContainer.addView(noDataView)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ConsultExchangeActivity, "Error al consultar: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
