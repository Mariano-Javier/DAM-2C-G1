package dam_2c_1c25.g1.clubdeportivo.ui.pagoactividades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.database.ActividadDao
import dam_2c_1c25.g1.clubdeportivo.data.database.ClienteDao
import dam_2c_1c25.g1.clubdeportivo.data.database.DescuentoDao
import dam_2c_1c25.g1.clubdeportivo.data.model.Actividad
import dam_2c_1c25.g1.clubdeportivo.data.model.Cliente
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import android.view.ViewGroup
import dam_2c_1c25.g1.clubdeportivo.data.database.PagoActividadDao
import dam_2c_1c25.g1.clubdeportivo.ui.comprobantepago.ComprobantePagoActivity


class PagoActividadesActivity : BaseActivity() {

    // Views
    private lateinit var spinnerTipoBusqueda: Spinner
    private lateinit var tilBusqueda: TextInputLayout
    private lateinit var etBusqueda: TextInputEditText
    private lateinit var btnBuscarCliente: Button
    private lateinit var tvErrorCliente: TextView
    private lateinit var layoutInfoCliente: LinearLayout
    private lateinit var tvNombreCliente: TextView
    private lateinit var tvDniCliente: TextView
    private lateinit var tvTipoCliente: TextView
    private lateinit var cardActividades: MaterialCardView
    private lateinit var containerActividades: LinearLayout
    private lateinit var cardResumen: MaterialCardView
    private lateinit var tvActividadesSeleccionadas: TextView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvDescuento: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnContinuar: Button
    private lateinit var tvFechaSeleccionada: TextView
    private lateinit var btnSeleccionarFecha: Button
    private var fechaSeleccionada: String = "" // Formato: "yyyy-MM-dd"
    private lateinit var rgMetodoPago: RadioGroup
    private lateinit var rbEfectivo: RadioButton
    private lateinit var rbTransferencia: RadioButton
    private lateinit var rbTarjeta: RadioButton
    private var descuentoAplicado: Double = 0.0
    private lateinit var btnCancelar: Button

    // DAOs
    private lateinit var clienteDao: ClienteDao
    private lateinit var actividadDao: ActividadDao
    private lateinit var descuentoDao: DescuentoDao

    // Data
    private var clienteSeleccionado: Cliente? = null
    private val actividadesSeleccionadas = mutableListOf<Actividad>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_pago_actividades)
        supportActionBar?.title = "Pago de Actividades"

        // Inicializar DAOs
        clienteDao = ClienteDao(this)
        actividadDao = ActividadDao(this)
        descuentoDao = DescuentoDao(this)

        // Inicializar vistas
        initViews()
        setupSpinner()
        setupDatePicker()
        setupMetodosPago()
        setupButtons()

        // Verificar si viene de registro y cargar cliente automáticamente
        val cliente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("cliente", Cliente::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Cliente>("cliente")
        }

        if (cliente != null) {
            mostrarInfoCliente(cliente)
            cargarActividades()
        } else {
            val dni = intent.getStringExtra("dni")
            if (!dni.isNullOrEmpty()) {
                val clienteBuscado = clienteDao.obtenerClientePorDni(dni)
                if (clienteBuscado != null) {
                    mostrarInfoCliente(clienteBuscado)
                    cargarActividades()
                }
            }
        }
    }

    private fun initViews() {
        spinnerTipoBusqueda = findViewById(R.id.spinnerTipoBusqueda)
        tilBusqueda = findViewById(R.id.tilBusqueda)
        etBusqueda = findViewById(R.id.etBusqueda)
        btnBuscarCliente = findViewById(R.id.btnBuscarCliente)
        tvErrorCliente = findViewById(R.id.tvErrorCliente)
        layoutInfoCliente = findViewById(R.id.layoutInfoCliente)
        tvNombreCliente = findViewById(R.id.tvNombreCliente)
        tvDniCliente = findViewById(R.id.tvDniCliente)
        tvTipoCliente = findViewById(R.id.tvTipoCliente)
        cardActividades = findViewById(R.id.cardActividades)
        containerActividades = findViewById(R.id.containerActividades)
        cardResumen = findViewById(R.id.cardResumen)
        tvActividadesSeleccionadas = findViewById(R.id.tvActividadesSeleccionadas)
        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvDescuento = findViewById(R.id.tvDescuento)
        tvTotal = findViewById(R.id.tvTotal)
        btnContinuar = findViewById(R.id.btnContinuar)
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada)
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha)
        rgMetodoPago = findViewById(R.id.rgMetodoPago)
        rbEfectivo = findViewById(R.id.rbEfectivo)
        rbTransferencia = findViewById(R.id.rbTransferencia)
        rbTarjeta = findViewById(R.id.rbTarjeta)
        btnCancelar = findViewById(R.id.btnCancelar)
    }

    private fun setupSpinner() {
        val opcionesBusqueda = arrayOf("DNI", "ID")

        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            opcionesBusqueda
        ) {

        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoBusqueda.adapter = adapter

        spinnerTipoBusqueda.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (view is TextView) {
                    view.setTextColor(ContextCompat.getColor(this@PagoActividadesActivity, R.color.white))
                }
                tilBusqueda.hint = when (position) {
                    0 -> "Ingrese DNI del cliente"
                    1 -> "Ingrese ID del cliente"
                    else -> ""
                }
                etBusqueda.text?.clear()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupButtons() {
        btnBuscarCliente.setOnClickListener {
            buscarCliente()
        }

        btnContinuar.setOnClickListener {
            if (clienteSeleccionado == null) {
                Toast.makeText(this, "Seleccione un cliente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (actividadesSeleccionadas.isEmpty()) {
                Toast.makeText(this, "Seleccione al menos una actividad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val metodoPago = when (rgMetodoPago.checkedRadioButtonId) {
                R.id.rbEfectivo -> "Efectivo"
                R.id.rbTransferencia -> "Transferencia"
                R.id.rbTarjeta -> "Tarjeta"
                else -> {
                    Toast.makeText(this, "Seleccione un método de pago", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Calcular montos
            val subtotal = actividadesSeleccionadas.sumOf { it.precio }
            val descuento = subtotal * descuentoAplicado
            val total = subtotal - descuento

            // Registrar el pago en la base de datos
            val pagoActividadDao = PagoActividadDao(this)
            val resultado = pagoActividadDao.registrarPagoActividad(
                idCliente = clienteSeleccionado!!.id,
                monto = total,
                medioPago = metodoPago,
                fechaPago = fechaSeleccionada,
                periodoInicio = fechaSeleccionada // Puedes ajustar esto según tu lógica de periodos
            )

            if (resultado != -1L) {
                // Crear y mostrar comprobante solo si el pago se registró correctamente
                val intent = Intent(this, ComprobantePagoActivity::class.java).apply {
                    putExtra("cliente", clienteSeleccionado)
                    putExtra("actividades", ArrayList(actividadesSeleccionadas))
                    putExtra("fecha", fechaSeleccionada)
                    putExtra("metodoPago", metodoPago)
                    putExtra("subtotal", subtotal)
                    putExtra("descuento", descuento)
                    putExtra("total", total)
                }
                startActivity(intent)
                limpiarFormulario()
            } else {
                Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
            }

        }

        btnCancelar.setOnClickListener {
            limpiarFormulario()
            finish() // Cierra la actividad y vuelve al menú anterior
        }
    }

    private fun buscarCliente() {
        val tipoBusqueda = spinnerTipoBusqueda.selectedItemPosition
        val valorBusqueda = etBusqueda.text.toString().trim()

        if (valorBusqueda.isEmpty()) {
            tilBusqueda.error = when (tipoBusqueda) {
                0 -> "Ingrese un DNI válido"
                1 -> "Ingrese un ID válido"
                else -> "Ingrese un valor válido"
            }
            return
        }
        tilBusqueda.error = null

        val cliente = when (tipoBusqueda) {
            0 -> clienteDao.obtenerClientePorDni(valorBusqueda)
            1 -> try {
                clienteDao.obtenerClientePorId(valorBusqueda.toInt())
            } catch (e: NumberFormatException) {
                null
            }
            else -> null
        }

        if (cliente == null) {
            mostrarErrorCliente("Cliente no encontrado")
            return
        }

        if (cliente.esSocio) {
            mostrarErrorCliente("Este cliente es socio. Debe pagar la cuota mensual.")
            return
        }

        mostrarInfoCliente(cliente)
    }

    private fun mostrarErrorCliente(mensaje: String) {
        tvErrorCliente.text = mensaje
        tvErrorCliente.visibility = View.VISIBLE
        layoutInfoCliente.visibility = View.GONE
        cardActividades.visibility = View.GONE
        cardResumen.visibility = View.GONE
        actividadesSeleccionadas.clear()
        containerActividades.removeAllViews()
    }

    private fun mostrarInfoCliente(cliente: Cliente) {
        clienteSeleccionado = cliente

        tvErrorCliente.visibility = View.GONE
        layoutInfoCliente.visibility = View.VISIBLE

        tvNombreCliente.text = "${cliente.nombre} ${cliente.apellido}"
        tvDniCliente.text = "DNI: ${cliente.dni}"
        tvTipoCliente.text = if (cliente.esApto) "Cliente con apto físico" else "Cliente sin apto físico"

        val color = if (cliente.esApto) R.color.success_color else R.color.warning_color
        tvTipoCliente.setTextColor(ContextCompat.getColor(this, color))

        // Ocultar teclado
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etBusqueda.windowToken, 0)

        // Cargar actividades
        cargarActividades()
    }

    private fun cargarActividades() {
        val actividades = actividadDao.obtenerTodasActividades()
        containerActividades.removeAllViews()
        actividadesSeleccionadas.clear()

        actividades.forEachIndexed { index, actividad ->
            val view = LayoutInflater.from(this).inflate(
                R.layout.item_actividad_pago_simple,
                containerActividades,
                false
            )

            val tvNombre = view.findViewById<TextView>(R.id.tvNombreActividad)
            val tvPrecio = view.findViewById<TextView>(R.id.tvPrecioActividad)

            tvNombre.text = actividad.nombre
            tvPrecio.text = "$${"%.2f".format(actividad.precio)}"

            // Manejar selección
            view.setOnClickListener {
                if (actividadesSeleccionadas.contains(actividad)) {
                    actividadesSeleccionadas.remove(actividad)
                    view.setBackgroundColor(ContextCompat.getColor(this@PagoActividadesActivity, android.R.color.transparent))
                } else {
                    actividadesSeleccionadas.add(actividad)
                    view.setBackgroundColor(ContextCompat.getColor(this@PagoActividadesActivity, R.color.selected_color))
                }
                actualizarResumen()
            }

            containerActividades.addView(view)

            // Añadir divisor entre items (excepto el último)
            if (index < actividades.size - 1) {
                val divider = View(this@PagoActividadesActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    ).apply {
                        setMargins(0, 8, 0, 8)
                    }
                    setBackgroundColor(ContextCompat.getColor(this@PagoActividadesActivity, R.color.gray_divider))
                }
                containerActividades.addView(divider)
            }
        }

        cardActividades.visibility = View.VISIBLE
        cardResumen.visibility = View.GONE
    }

    private fun actualizarResumen() {
        if (actividadesSeleccionadas.isNotEmpty()) {
            val subtotal = actividadesSeleccionadas.sumOf { it.precio }
            val descuento = subtotal * descuentoAplicado
            val total = subtotal - descuento

            tvActividadesSeleccionadas.text = actividadesSeleccionadas.joinToString("\n") {
                "• ${it.nombre} ($${"%.2f".format(it.precio)})"
            }

            tvSubtotal.text = "Subtotal: $${"%.2f".format(subtotal)}"
            tvDescuento.text = "Descuento (${(descuentoAplicado * 100).toInt()}%): -$${"%.2f".format(descuento)}"
            tvTotal.text = "Total: $${"%.2f".format(total)}"

            cardResumen.visibility = View.VISIBLE
        } else {
            cardResumen.visibility = View.GONE
        }
    }

    private fun setupDatePicker() {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dbDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Fecha actual por defecto
        fechaSeleccionada = dbDateFormatter.format(calendar.time)
        tvFechaSeleccionada.text = dateFormatter.format(calendar.time)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            // Actualizar ambas representaciones de la fecha
            fechaSeleccionada = dbDateFormatter.format(calendar.time)
            tvFechaSeleccionada.text = dateFormatter.format(calendar.time)
        }

        val clickListener = View.OnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        tvFechaSeleccionada.setOnClickListener(clickListener)
        btnSeleccionarFecha.setOnClickListener(clickListener)
    }

    private fun setupMetodosPago() {
        // Obtener descuentos de la base de datos
        val descuentos = descuentoDao.obtenerTodosDescuentos()

        // Configurar listeners
        rgMetodoPago.setOnCheckedChangeListener { _, checkedId ->
            val metodoPago = when (checkedId) {
                R.id.rbEfectivo -> "Efectivo"
                R.id.rbTransferencia -> "Transferencia"
                R.id.rbTarjeta -> "Tarjeta"
                else -> ""
            }

            // Buscar el descuento correspondiente
            val descuento = descuentos.find { it.tipoPago == metodoPago }?.valorDescuento ?: 0.0
            descuentoAplicado = descuento
            actualizarResumen()
        }
    }

    private fun limpiarFormulario() {
        // Limpiar selección de cliente
        clienteSeleccionado = null
        etBusqueda.text?.clear()
        layoutInfoCliente.visibility = View.GONE
        tvErrorCliente.visibility = View.GONE

        // Limpiar actividades
        actividadesSeleccionadas.clear()
        containerActividades.removeAllViews()
        cardActividades.visibility = View.GONE

        // Limpiar resumen
        cardResumen.visibility = View.GONE
        tvActividadesSeleccionadas.text = ""
        tvSubtotal.text = ""
        tvDescuento.text = ""
        tvTotal.text = ""

        // Resetear método de pago
        rgMetodoPago.clearCheck()
        descuentoAplicado = 0.0

        // Resetear fecha
        val calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dbDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fechaSeleccionada = dbDateFormatter.format(calendar.time)
        tvFechaSeleccionada.text = dateFormatter.format(calendar.time)
    }

}