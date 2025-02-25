package com.example.appintercambios
import android.os.StrictMode

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class CreateExchangeActivity : AppCompatActivity() {

    // Variables globales
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
    private lateinit var database: DatabaseReference
    private var uniqueKey: String = ""
    private lateinit var userEmail: String // Nuevo campo para guardar el email del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exchange)

        // Validar sesión y obtener el email del usuario
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        userEmail = sharedPref.getString("user_email", null) ?: run {
            Toast.makeText(this, "Debe iniciar sesión para continuar", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inicializar vistas
        initViews()

        // Configurar Firebase
        database = FirebaseDatabase.getInstance().getReference("exchanges")

        // Generar una clave única
        generateUniqueKey()

        // Configurar eventos
        configureEventListeners()
    }

    private fun initViews() {
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
    }


    fun sendEmail(context: Context, toEmail: String, subject: String, messageBody: String) {
        val email = "erodriguezm1406@gmail.com"
        val password = "pupy wjpl vlkj xnzx"

        // Configuración del policy para evitar problemas de red en el hilo principal (no recomendado para producción)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "465")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(email, password)
            }
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(email))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                setSubject(subject)
                setText(messageBody)
            }

            Transport.send(message)
            Toast.makeText(context, "Correo enviado a $toEmail", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error al enviar correo: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }





    private fun configureEventListeners() {
        // Botón para agregar correos electrónicos
        btnAddEmail.setOnClickListener { addEmailField() }

        // Selección de fechas
        tvDeadline.setOnClickListener { showDatePickerDialog(tvDeadline) }
        tvExchangeDate.setOnClickListener { showDateTimePickerDialog(tvExchangeDate) }

        // Botón para crear intercambio
        btnCreateExchange.setOnClickListener { createExchange() }
    }

    private fun generateUniqueKey() {
        uniqueKey = (10000..99999).random().toString()
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
        // Obtener todos los correos electrónicos
        val emails = mutableListOf<String>()

        for (i in 0 until emailsContainer.childCount) {
            val emailField = emailsContainer.getChildAt(i) as? EditText
            val email = emailField?.text.toString()
            if (email.isNotEmpty()) emails.add(email)
        }

        // Validar si hay correos
        if (emails.isEmpty()) {
            Toast.makeText(this, "Por favor, agregue al menos un correo", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener otros datos
        val theme = spTheme.selectedItem.toString()
        val maxAmount = etMaxAmount.text.toString().toDoubleOrNull()
        val deadline = tvDeadline.text.toString()
        val exchangeDate = tvExchangeDate.text.toString()
        val location = etLocation.text.toString()
        val comments = etComments.text.toString()

        // Validar los campos obligatorios
        if (theme.isEmpty() || maxAmount == null || deadline.isEmpty() || exchangeDate.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un objeto para el intercambio
        val exchange = mapOf(
            "uniqueKey" to uniqueKey,
            "creatorEmail" to userEmail,
            "emails" to emails,
            "theme" to theme,
            "maxAmount" to maxAmount,
            "deadline" to deadline,
            "exchangeDate" to exchangeDate,
            "location" to location,
            "comments" to comments
        )

        // Guardar en Firebase
        database.child(uniqueKey).setValue(exchange)
            .addOnSuccessListener {
                Toast.makeText(this, "Intercambio creado exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al guardar: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        // Enviar correos electrónicos a los participantes
        val subject = "Invitación al intercambio"
        val messageBody = "¡Has sido invitado al intercambio con clave $uniqueKey!"

        emails.forEach { email ->
            sendEmail(this, email, subject, messageBody)
        }
    }
}
