package com.example.appintercambios

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CreateExchangeActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private lateinit var tvUniqueKey: TextView
    private lateinit var emailsContainer: LinearLayout
    private lateinit var btnAddEmail: Button
    private lateinit var spTheme: Spinner
    private lateinit var etMaxAmount: EditText
    private lateinit var tvDeadline: TextView
    private lateinit var tvExchangeDate: TextView
    private lateinit var etLocation: EditText
    private lateinit var etComments: EditText
    private lateinit var btnCreateExchange: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exchange)

        // Inicializar vistas
        tvUniqueKey = findViewById(R.id.tvUniqueKey)
        emailsContainer = findViewById(R.id.emailsContainer)
        btnAddEmail = findViewById(R.id.btnAddEmail)
        spTheme = findViewById(R.id.spTheme)
        etMaxAmount = findViewById(R.id.etMaxAmount)
        tvDeadline = findViewById(R.id.tvDeadline)
        tvExchangeDate = findViewById(R.id.tvExchangeDate)
        etLocation = findViewById(R.id.etLocation)
        etComments = findViewById(R.id.etComments)
        btnCreateExchange = findViewById(R.id.btnCreateExchange)

        // Generar una clave única
        generateUniqueKey()

        // Configurar botón para agregar más campos de correo
        btnAddEmail.setOnClickListener { addEmailField() }

        // Configurar selección de fechas
        tvDeadline.setOnClickListener { showDatePickerDialog(tvDeadline) }
        tvExchangeDate.setOnClickListener { showDateTimePickerDialog(tvExchangeDate) }

        // Configurar botón para crear intercambio
        btnCreateExchange.setOnClickListener { createExchange() }
    }

    private fun generateUniqueKey() {
        val uniqueKey = UUID.randomUUID().toString().take(8).uppercase(Locale.ROOT)
        tvUniqueKey.text = "Clave única: $uniqueKey"
    }

    private fun addEmailField() {
        val emailField = EditText(this).apply {
            hint = "Correo electrónico"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 8, 0, 8) }
        }
        emailsContainer.addView(emailField)
    }

    private fun showDatePickerDialog(targetView: TextView) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            targetView.text = formattedDate
        }, year, month, day).show()
    }

    private fun showDateTimePickerDialog(targetView: TextView) {
        showDatePickerDialog(targetView)
        TimePickerDialog(this, { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            val formattedDateTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(calendar.time)
            targetView.text = formattedDateTime
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    private fun createExchange() {
        // Obtener correos electrónicos
        val emails = mutableListOf<String>()
        for (i in 0 until emailsContainer.childCount) {
            val emailField = emailsContainer.getChildAt(i) as EditText
            val email = emailField.text.toString()
            if (email.isNotEmpty()) emails.add(email)
        }

        // Validar si hay correos
        if (emails.isEmpty()) {
            Toast.makeText(this, "Por favor, agregue al menos un correo", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Se han enviado invitaciones a: $emails", Toast.LENGTH_SHORT).show()
    }
}
