package dam_2c_1c25.g1.clubdeportivo.ui.registrarempleado

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dam_2c_1c25.g1.clubdeportivo.R
import java.util.Calendar
import java.util.Locale
import dam_2c_1c25.g1.clubdeportivo.data.database.EmpleadoDao
import android.widget.Toast
import dam_2c_1c25.g1.clubdeportivo.data.model.Empleado

class RegistrarEmpleadoActivity : AppCompatActivity() {

    // Variable para guardar la fecha formateada como "yyyy-MM-dd"
    private var fechaNacimientoISO: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_empleado)

        //Manejo del spinner
        val spinnerRol = findViewById<Spinner>(R.id.spinnerRol)
        val roles = listOf("administrador", "profesor")

        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            roles
        ) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(ContextCompat.getColor(context, R.color.black)) // texto seleccionado
                return view
            }

            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(ContextCompat.getColor(context, R.color.black)) // opciones
                view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white)) // fondo blanco
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRol.adapter = adapter
        //Fin manejo del spinner

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

        // Cierra la actividad sin guardar
        val botonCancelar = findViewById<Button>(R.id.buttonCancelar)
        botonCancelar.setOnClickListener {
            finish()
        }
        //Registrar un nuevo empleado
        val botonRegistrar = findViewById<Button>(R.id.buttonRegistrar)
        val dao = EmpleadoDao(this)

        botonRegistrar.setOnClickListener {
            val nombre = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTextNombre).text.toString().trim()
            val apellido = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTextApellido).text.toString().trim()
            val dni = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTextDni).text.toString().trim()
            val email = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTextEmail).text.toString().trim()
            val telefono = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTextTelefono).text.toString().trim()
            val rol = spinnerRol.selectedItem.toString()
            val usuario = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTextUsuario).text.toString().trim()
            val contrasenia = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTextContrasenia).text.toString().trim()

            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || email.isEmpty()
                || telefono.isEmpty() || fechaNacimientoISO.isNullOrEmpty()
                || usuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dao.existeUsuario(usuario)) {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoEmpleado = Empleado(
                id = 0, // se autogenera
                nombre = nombre,
                apellido = apellido,
                dni = dni,
                email = email,
                telefono = telefono,
                fechaNac = fechaNacimientoISO!!,
                rol = rol,
                usuario = usuario,
                contrasenia = contrasenia
            )

            val exito = dao.insertarEmpleado(nuevoEmpleado)
            if (exito) {
                Toast.makeText(this, "Empleado registrado con éxito", Toast.LENGTH_SHORT).show()
                finish() // cerrar la actividad
            } else {
                Toast.makeText(this, "Error al registrar empleado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
