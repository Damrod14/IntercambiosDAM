package com.example.appintercambios

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ConsultExchangeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExchangeAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consult_exchanges)

        // Inicializar referencia a Firebase
        database = FirebaseDatabase.getInstance().getReference("exchanges")

        // Obtener el correo del usuario desde SharedPreferences
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", null)

        if (userEmail == null) {
            Toast.makeText(this, "Error: no se pudo obtener el correo del usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rvExchanges)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Lista para guardar los intercambios obtenidos
        val exchangeList = mutableListOf<Exchange>()

        // Leer datos de Firebase
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (exchangeSnapshot in snapshot.children) {
                        val creatorEmail = exchangeSnapshot.child("creatorEmail").getValue(String::class.java)
                        val emailsSnapshot = exchangeSnapshot.child("emails")
                        val emailsList = mutableListOf<String>()
                        Log.d("ExchangeInfo", "Intercambio añadido: $creatorEmail")

                        // Extraer correos de la lista "emails"
                        emailsSnapshot.children.forEach {
                            val email = it.getValue(String::class.java)
                            if (!email.isNullOrEmpty() && email != "null") {
                                emailsList.add(email)
                            }
                        }

                        // Verificar si el usuario participa o creó el intercambio
                        if (userEmail in emailsList || creatorEmail == userEmail) {
                            val exchangeName = exchangeSnapshot.child("theme").getValue(String::class.java) ?: "Sin tema"
                            val exchangeDate = exchangeSnapshot.child("exchangeDate").getValue(String::class.java) ?: "Sin fecha"
                            val exchangeLocation = exchangeSnapshot.child("location").getValue(String::class.java) ?: "Sin ubicación"
                            val exchangeMaxAmount = exchangeSnapshot.child("maxAmount").getValue(Int::class.java) ?: 0
                            val exchangeComments = exchangeSnapshot.child("comments").getValue(String::class.java) ?: "Sin comentarios"
                            val exchangeTheme = exchangeSnapshot.child("theme").getValue(String::class.java) ?: "Sin tema"
                            val exchangeUniqueKey = exchangeSnapshot.child("uniqueKey").getValue(String::class.java) ?: "Sin clave única"

                            // Añadir intercambio a la lista
                            exchangeList.add(
                                Exchange(
                                    name = exchangeName,
                                    date = exchangeDate,
                                    location = exchangeLocation,
                                    maxAmount = exchangeMaxAmount,
                                    theme = exchangeTheme,
                                    comments = exchangeComments,
                                    emails = emailsList,
                                    uniqueKey = exchangeUniqueKey
                                )
                            )
                        }
                    }

                    // Actualizar RecyclerView con los intercambios obtenidos
                    adapter = ExchangeAdapter(exchangeList)
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this@ConsultExchangeActivity, "No se encontraron intercambios", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", error.message)
                Toast.makeText(this@ConsultExchangeActivity, "Error al cargar los intercambios", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
