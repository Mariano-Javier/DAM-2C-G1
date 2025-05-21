package dam_2c_1c25.g1.clubdeportivo.ui.registrarcliente

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.database.ClienteDao
import dam_2c_1c25.g1.clubdeportivo.data.model.Cliente
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity
import java.util.Calendar
import java.util.Locale

class RegistrarClienteActivity : BaseActivity() {
    // Variable para guardar la fecha formateada como "yyyy-MM-dd"
    private var fechaNacimientoISO: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_registrar_cliente)

        // Cambia el título del toolbar
        supportActionBar?.title = "Registrar Cliente"

        // Cierra la actividad sin guardar
        val botonCancelar = findViewById<Button>(R.id.buttonCancelar)
        botonCancelar.setOnClickListener {
            finish()
        }

        // Referencia al TextView para la Fecha de nacimiento
        val textViewFechaNac = findViewById<TextView>(R.id.textViewFechaNac)

        textViewFechaNac.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                val fechaMostrada = "$d/${m + 1}/$y"
                textViewFechaNac.text = fechaMostrada  // Mostrar al usuario en formato legible
                fechaNacimientoISO = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d)
            }, year, month, day)

            datePicker.show()
        }

        // Botón para registrar cliente
        val botonRegistrar = findViewById<Button>(R.id.buttonRegistrar)

        botonRegistrar.setOnClickListener {
            val nombre = findViewById<TextInputEditText>(R.id.editTextNombre).text.toString()
            val apellido = findViewById<TextInputEditText>(R.id.editTextApellido).text.toString()
            val dni = findViewById<TextInputEditText>(R.id.editTextDni).text.toString()
            val email = findViewById<TextInputEditText>(R.id.editTextEmail).text.toString()
            val telefono = findViewById<TextInputEditText>(R.id.editTextTelefono).text.toString()
            val esSocio = findViewById<CheckBox>(R.id.checkboxEsSocio).isChecked
            val esApto = findViewById<CheckBox>(R.id.checkboxEsApto).isChecked

            // Validar campos obligatorios
            if (nombre.isBlank() || apellido.isBlank() || dni.isBlank() || email.isBlank() || telefono.isBlank() || fechaNacimientoISO == null) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cliente = Cliente(
                nombre = nombre,
                apellido = apellido,
                dni = dni,
                email = email,
                telefono = telefono,
                fechaNac = fechaNacimientoISO!!,
                esSocio = esSocio,
                esApto = esApto
            )

            val dao = ClienteDao(this)
            val resultado = dao.insertarCliente(cliente)

            if (resultado != -1L) {
                Toast.makeText(this, "Cliente registrado con éxito", Toast.LENGTH_SHORT).show()
                finish() // opcional: cerrar actividad al registrar con éxito
            } else {
                Toast.makeText(this, "Error al registrar cliente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
