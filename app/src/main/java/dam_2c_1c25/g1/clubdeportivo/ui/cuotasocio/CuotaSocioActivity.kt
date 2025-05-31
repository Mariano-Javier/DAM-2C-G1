package dam_2c_1c25.g1.clubdeportivo.ui.cuotasocio

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.database.ClienteDao
import dam_2c_1c25.g1.clubdeportivo.data.database.CuotaSocioDao
import dam_2c_1c25.g1.clubdeportivo.data.database.DescuentoDao
import dam_2c_1c25.g1.clubdeportivo.data.database.PagoCuotaDao
import dam_2c_1c25.g1.clubdeportivo.data.model.Cliente
import dam_2c_1c25.g1.clubdeportivo.data.model.CuotaSocio
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity
import dam_2c_1c25.g1.clubdeportivo.ui.comprobantepago.ComprobantePagoActivity
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.widget.Toast

class CuotaSocioActivity : BaseActivity() {

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
    private lateinit var cardCuota: MaterialCardView
    private lateinit var spinnerTipoCuota: Spinner
    private lateinit var tvFechaInicio: TextView
    private lateinit var btnSeleccionarFechaInicio: Button
    private lateinit var tvDuracionLabel: TextView
    private lateinit var spinnerDuracion: Spinner
    private lateinit var tvFechaVencimiento: TextView
    private lateinit var cardResumen: MaterialCardView
    private lateinit var tvFechaPago: TextView
    private lateinit var btnSeleccionarFechaPago: Button
    private lateinit var tvDetallesCuota: TextView
    private lateinit var rgMetodoPago: RadioGroup
    private lateinit var rbEfectivo: RadioButton
    private lateinit var rbTransferencia: RadioButton
    private lateinit var rbTarjeta: RadioButton
    private lateinit var tvSubtotal: TextView
    private lateinit var tvDescuento: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnContinuar: Button
    private lateinit var btnCancelar: Button

    // DAOs
    private lateinit var clienteDao: ClienteDao
    private lateinit var cuotaSocioDao: CuotaSocioDao
    private lateinit var descuentoDao: DescuentoDao

    // Data
    private var clienteSeleccionado: Cliente? = null
    private var cuotasDisponibles: List<CuotaSocio> = emptyList()
    private var cuotaSeleccionada: CuotaSocio? = null
    private var fechaInicio: String = "" // Formato: "yyyy-MM-dd"
    private var fechaPago: String = "" // Formato: "yyyy-MM-dd"
    private var descuentoAplicado: Double = 0.0
    private var duracionSeleccionada: Int = 1

    // Formateadores de fecha
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val dbDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_cuota_socio)
        supportActionBar?.title = "Pago de Cuotas"

        // Inicializar DAOs
        clienteDao = ClienteDao(this)
        cuotaSocioDao = CuotaSocioDao(this)
        descuentoDao = DescuentoDao(this)

        // Inicializar vistas
        initViews()
        setupSpinners()
        setupDatePickers()
        setupMetodosPago()
        setupButtons()
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
        cardCuota = findViewById(R.id.cardCuota)
        spinnerTipoCuota = findViewById(R.id.spinnerTipoCuota)
        tvFechaInicio = findViewById(R.id.tvFechaInicio)
        btnSeleccionarFechaInicio = findViewById(R.id.btnSeleccionarFechaInicio)
        tvDuracionLabel = findViewById(R.id.tvDuracionLabel)
        spinnerDuracion = findViewById(R.id.spinnerDuracion)
        tvFechaVencimiento = findViewById(R.id.tvFechaVencimiento)
        cardResumen = findViewById(R.id.cardResumen)
        tvFechaPago = findViewById(R.id.tvFechaPago)
        btnSeleccionarFechaPago = findViewById(R.id.btnSeleccionarFechaPago)
        tvDetallesCuota = findViewById(R.id.tvDetallesCuota)
        rgMetodoPago = findViewById(R.id.rgMetodoPago)
        rbEfectivo = findViewById(R.id.rbEfectivo)
        rbTransferencia = findViewById(R.id.rbTransferencia)
        rbTarjeta = findViewById(R.id.rbTarjeta)
        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvDescuento = findViewById(R.id.tvDescuento)
        tvTotal = findViewById(R.id.tvTotal)
        btnContinuar = findViewById(R.id.btnContinuar)
        btnCancelar = findViewById(R.id.btnCancelar)
    }

    private fun setupSpinners() {
        // Spinner de tipo de búsqueda
        val opcionesBusqueda = arrayOf("DNI", "ID")
        val adapterBusqueda = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesBusqueda)
        adapterBusqueda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoBusqueda.adapter = adapterBusqueda

        spinnerTipoBusqueda.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tilBusqueda.hint = when (position) {
                    0 -> "Ingrese DNI del socio"
                    1 -> "Ingrese ID del socio"
                    else -> ""
                }
                etBusqueda.text?.clear()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Cargar tipos de cuota desde la base de datos
        cuotasDisponibles = cuotaSocioDao.obtenerTodasCuotas()
        val descripcionesCuotas = cuotasDisponibles.map { it.descripcion }

        val adapterCuotas = ArrayAdapter(this, android.R.layout.simple_spinner_item, descripcionesCuotas)
        adapterCuotas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoCuota.adapter = adapterCuotas

        spinnerTipoCuota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cuotaSeleccionada = cuotasDisponibles[position]
                actualizarDuracionLabel()
                calcularFechaVencimiento()
                actualizarResumen()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Spinner de duración (1-6 meses/años)
        val duraciones = (1..6).toList()
        val adapterDuracion = ArrayAdapter(this, android.R.layout.simple_spinner_item, duraciones)
        adapterDuracion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDuracion.adapter = adapterDuracion

        spinnerDuracion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                duracionSeleccionada = position + 1
                calcularFechaVencimiento()
                actualizarResumen()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun actualizarDuracionLabel() {
        val texto = if (cuotaSeleccionada?.descripcion == "Mensual") {
            "Meses de suscripción:"
        } else {
            "Años de suscripción:"
        }
        tvDuracionLabel.text = texto
    }

    private fun setupDatePickers() {
        // Fecha de inicio del periodo
        val dateSetListenerInicio = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }
            fechaInicio = dbDateFormatter.format(calendar.time)
            tvFechaInicio.text = dateFormatter.format(calendar.time)
            calcularFechaVencimiento()
            actualizarResumen()
        }

        btnSeleccionarFechaInicio.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                dateSetListenerInicio,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Fecha de pago (por defecto hoy)
        val dateSetListenerPago = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }
            fechaPago = dbDateFormatter.format(calendar.time)
            tvFechaPago.text = dateFormatter.format(calendar.time)
        }

        // Establecer fecha de pago por defecto (hoy)
        val calendar = Calendar.getInstance()
        fechaPago = dbDateFormatter.format(calendar.time)
        tvFechaPago.text = dateFormatter.format(calendar.time)

        btnSeleccionarFechaPago.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListenerPago,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun calcularFechaVencimiento() {
        if (fechaInicio.isEmpty() || cuotaSeleccionada == null) return

        try {
            val fecha = dbDateFormatter.parse(fechaInicio) ?: return
            val calendar = Calendar.getInstance().apply { time = fecha }

            if (cuotaSeleccionada?.descripcion == "Mensual") {
                calendar.add(Calendar.MONTH, duracionSeleccionada)
            } else {
                calendar.add(Calendar.YEAR, duracionSeleccionada)
            }

            val fechaVencimiento = dbDateFormatter.format(calendar.time)
            tvFechaVencimiento.text = dateFormatter.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun setupButtons() {
        btnBuscarCliente.setOnClickListener {
            buscarCliente()
        }

        btnContinuar.setOnClickListener {
            if (validarDatos()) {
                registrarPago()
            }
        }

        btnCancelar.setOnClickListener {
            limpiarFormulario()
            finish()
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

        if (!cliente.esSocio) {
            mostrarErrorCliente("Este cliente no es socio. Debe registrarse como socio primero.")
            return
        }

        mostrarInfoCliente(cliente)
    }

    private fun mostrarErrorCliente(mensaje: String) {
        tvErrorCliente.text = mensaje
        tvErrorCliente.visibility = View.VISIBLE
        layoutInfoCliente.visibility = View.GONE
        cardCuota.visibility = View.GONE
        cardResumen.visibility = View.GONE
    }

    private fun mostrarInfoCliente(cliente: Cliente) {
        clienteSeleccionado = cliente

        tvErrorCliente.visibility = View.GONE
        layoutInfoCliente.visibility = View.VISIBLE

        tvNombreCliente.text = "${cliente.nombre} ${cliente.apellido}"
        tvDniCliente.text = "DNI: ${cliente.dni}"
        tvTipoCliente.text = if (cliente.esApto) "Socio con apto físico" else "Socio sin apto físico"

        val color = if (cliente.esApto) R.color.success_color else R.color.warning_color
        tvTipoCliente.setTextColor(ContextCompat.getColor(this, color))

        // Mostrar sección de cuota
        cardCuota.visibility = View.VISIBLE
        cardResumen.visibility = View.GONE
    }

    private fun actualizarResumen() {
        if (cuotaSeleccionada == null || fechaInicio.isEmpty() || tvFechaVencimiento.text.isEmpty()) {
            cardResumen.visibility = View.GONE
            return
        }

        val montoPorPeriodo = cuotaSeleccionada?.monto ?: 0.0
        val subtotal = montoPorPeriodo * duracionSeleccionada
        val descuento = subtotal * descuentoAplicado
        val total = subtotal - descuento

        tvDetallesCuota.text = """
            Tipo de cuota: ${cuotaSeleccionada?.descripcion}
            Duración: $duracionSeleccionada ${if (cuotaSeleccionada?.descripcion == "Mensual") "mes(es)" else "año(s)"}
            Fecha inicio: ${tvFechaInicio.text}
            Fecha vencimiento: ${tvFechaVencimiento.text}
        """.trimIndent()

        tvSubtotal.text = "Subtotal: $${"%.2f".format(subtotal)}"
        tvDescuento.text = "Descuento (${(descuentoAplicado * 100).toInt()}%): -$${"%.2f".format(descuento)}"
        tvTotal.text = "Total: $${"%.2f".format(total)}"

        cardResumen.visibility = View.VISIBLE
    }

    private fun validarDatos(): Boolean {
        if (clienteSeleccionado == null) {
            Toast.makeText(this, "Seleccione un socio", Toast.LENGTH_SHORT).show()
            return false
        }

        if (fechaInicio.isEmpty()) {
            Toast.makeText(this, "Seleccione una fecha de inicio", Toast.LENGTH_SHORT).show()
            return false
        }

        if (rgMetodoPago.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Seleccione un método de pago", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registrarPago() {
        val metodoPago = when (rgMetodoPago.checkedRadioButtonId) {
            R.id.rbEfectivo -> "Efectivo"
            R.id.rbTransferencia -> "Transferencia"
            R.id.rbTarjeta -> "Tarjeta"
            else -> ""
        }

        val montoPorPeriodo = cuotaSeleccionada?.monto ?: 0.0
        val subtotal = montoPorPeriodo * duracionSeleccionada
        val descuento = subtotal * descuentoAplicado
        val total = subtotal - descuento

        // Obtener la fecha de vencimiento formateada correctamente
        val fechaVencimiento = try {
            dbDateFormatter.format(dateFormatter.parse(tvFechaVencimiento.text.toString()) ?: Date())
        } catch (e: Exception) {
            ""
        }

        // Verificar si ya existe un pago activo para este periodo
        val pagoCuotaDao = PagoCuotaDao(this)

        if (pagoCuotaDao.tienePagoActivo(clienteSeleccionado!!.id, fechaInicio, fechaVencimiento)) {
            val fechaFinPagoActivo = pagoCuotaDao.obtenerFechaFinPagoActivo(clienteSeleccionado!!.id)
            val mensaje = if (fechaFinPagoActivo != null) {
                val fechaFormateada = dateFormatter.format(dbDateFormatter.parse(fechaFinPagoActivo))
                "El socio ya tiene un pago activo hasta el $fechaFormateada. Debe elegir una fecha posterior."
            } else {
                "El socio ya tiene un pago activo para este periodo."
            }

            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
            return
        }

        // Registrar el pago en la base de datos
        val resultado = pagoCuotaDao.registrarPagoCuota(
            idCliente = clienteSeleccionado!!.id,
            monto = total,
            medioPago = metodoPago,
            fechaPago = fechaPago,
            periodoInicio = fechaInicio,
            periodoFin = fechaVencimiento,
            idCuota = cuotaSeleccionada?.id ?: 0
        )

        if (resultado != -1L) {
            // Mostrar comprobante
            val intent = Intent(this, ComprobantePagoActivity::class.java).apply {
                putExtra("cliente", clienteSeleccionado)
                putExtra("fecha", fechaPago)
                putExtra("metodoPago", metodoPago)
                putExtra("subtotal", subtotal)
                putExtra("descuento", descuento)
                putExtra("total", total)
                // Indicar que es pago de cuota
                putExtra("esCuotaSocio", true)
                // Datos específicos de la cuota
                putExtra("tipoCuota", cuotaSeleccionada?.descripcion ?: "")
                putExtra("duracion", duracionSeleccionada)
                putExtra("periodoInicio", fechaInicio)
                putExtra("periodoFin", tvFechaVencimiento.text.toString())
            }
            startActivity(intent)
            limpiarFormulario()
        } else {
            Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarFormulario() {
        // Limpiar selección de cliente
        clienteSeleccionado = null
        etBusqueda.text?.clear()
        layoutInfoCliente.visibility = View.GONE
        tvErrorCliente.visibility = View.GONE

        // Limpiar selección de cuota
        cardCuota.visibility = View.GONE
        fechaInicio = ""
        tvFechaInicio.text = "Seleccionar fecha"
        tvFechaVencimiento.text = ""

        // Limpiar resumen
        cardResumen.visibility = View.GONE
        tvDetallesCuota.text = ""
        tvSubtotal.text = ""
        tvDescuento.text = ""
        tvTotal.text = ""

        // Resetear método de pago
        rgMetodoPago.clearCheck()
        descuentoAplicado = 0.0

        // Resetear fecha de pago (a hoy)
        val calendar = Calendar.getInstance()
        fechaPago = dbDateFormatter.format(calendar.time)
        tvFechaPago.text = dateFormatter.format(calendar.time)
    }
}