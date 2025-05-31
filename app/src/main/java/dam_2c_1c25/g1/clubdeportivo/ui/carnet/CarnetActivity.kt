package dam_2c_1c25.g1.clubdeportivo.ui.carnet

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.database.ClienteDao
import dam_2c_1c25.g1.clubdeportivo.data.database.PagoActividadDao
import dam_2c_1c25.g1.clubdeportivo.data.database.PagoCuotaDao
import dam_2c_1c25.g1.clubdeportivo.data.model.Cliente
import dam_2c_1c25.g1.clubdeportivo.data.model.Pago
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CarnetActivity : BaseActivity() {

    private lateinit var spinnerTipoBusqueda: Spinner
    private lateinit var tilBusqueda: com.google.android.material.textfield.TextInputLayout
    private lateinit var etBusqueda: com.google.android.material.textfield.TextInputEditText
    private lateinit var btnBuscarCliente: Button
    private lateinit var tvErrorCliente: TextView
    private lateinit var layoutInfoCliente: LinearLayout
    private lateinit var tvNombreCliente: TextView
    private lateinit var tvDniCliente: TextView
    private lateinit var tvTipoCliente: TextView

    private lateinit var cardCarnet: androidx.cardview.widget.CardView
    private lateinit var ivLogo: ImageView
    private lateinit var tvNombreCarnet: TextView
    private lateinit var tvEstadoSocio: TextView
    private lateinit var ivCodigoQR: ImageView
    private lateinit var btnCompartirCarnet: Button
    private lateinit var btnFinalizarCarnet: Button
    private lateinit var layoutBotones: LinearLayout

    private lateinit var clienteDao: ClienteDao
    private lateinit var pagoCuotaDao: PagoCuotaDao
    private lateinit var pagoActividadDao: PagoActividadDao

    private var cliente: Cliente? = null
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_carnet)
        supportActionBar?.title = "Carnet"

        clienteDao = ClienteDao(this)
        pagoCuotaDao = PagoCuotaDao(this)
        pagoActividadDao = PagoActividadDao(this)

        initViews()
        setupSpinner()
        setupListeners()
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

        cardCarnet = findViewById(R.id.cardCarnet)
        ivLogo = findViewById(R.id.ivLogo)
        tvNombreCarnet = findViewById(R.id.tvNombreCarnet)
        tvEstadoSocio = findViewById(R.id.tvEstadoSocio)
        ivCodigoQR = findViewById(R.id.ivCodigoQR)

        layoutBotones = findViewById(R.id.layoutBotones)
        btnCompartirCarnet = findViewById(R.id.btnCompartirCarnet)
        btnFinalizarCarnet = findViewById(R.id.btnFinalizarCarnet)

        layoutBotones.visibility = View.GONE
        cardCarnet.visibility = View.GONE
    }

    private fun setupSpinner() {
        val opciones = arrayOf("DNI", "ID")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoBusqueda.adapter = adapter
    }

    private fun setupListeners() {
        btnBuscarCliente.setOnClickListener {
            val tipoBusqueda = spinnerTipoBusqueda.selectedItemPosition
            val valor = etBusqueda.text.toString().trim()

            if (valor.isEmpty()) {
                tilBusqueda.error = if (tipoBusqueda == 0) "Ingrese un DNI válido" else "Ingrese un ID válido"
                return@setOnClickListener
            }

            tilBusqueda.error = null

            cliente = when (tipoBusqueda) {
                0 -> clienteDao.obtenerClientePorDni(valor)
                1 -> valor.toIntOrNull()?.let { clienteDao.obtenerClientePorId(it) }
                else -> null
            }

            if (cliente == null) {
                mostrarError("Cliente no encontrado")
                return@setOnClickListener
            }

            val pago = if (cliente!!.esSocio) {
                val pagos = pagoCuotaDao.obtenerPagosCuotaPorCliente(cliente!!.id)
                pagos.maxByOrNull {
                    try {
                        formatter.parse(it.periodoFin ?: "")
                    } catch (e: Exception) {
                        Date(0)
                    }
                }
            } else {
                val pagos = pagoActividadDao.obtenerPagosActividadPorCliente(cliente!!.id)
                pagos.maxByOrNull {
                    try {
                        formatter.parse(it.periodoInicio ?: "")
                    } catch (e: Exception) {
                        Date(0)
                    }
                }
            }

            if (pago == null) {
                mostrarError("El cliente no tiene pagos registrados")
                return@setOnClickListener
            }

            mostrarInfoCliente(cliente!!)
            mostrarCarnet(cliente!!, pago)
        }

        btnCompartirCarnet.setOnClickListener {
            compartirCarnet()
        }

        btnFinalizarCarnet.setOnClickListener {
            finish()
        }
    }

    private fun mostrarError(mensaje: String) {
        tvErrorCliente.text = mensaje
        tvErrorCliente.visibility = View.VISIBLE
        layoutInfoCliente.visibility = View.GONE
        cardCarnet.visibility = View.GONE
        layoutBotones.visibility = View.GONE
    }

    private fun mostrarInfoCliente(c: Cliente) {
        layoutInfoCliente.visibility = View.VISIBLE
        tvErrorCliente.visibility = View.GONE
        tvNombreCliente.text = "${c.nombre} ${c.apellido}"
        tvDniCliente.text = "DNI: ${c.dni}"
        tvTipoCliente.text = if (c.esApto) "Posee apto físico" else "No posee apto físico"
        tvTipoCliente.setTextColor(
            ContextCompat.getColor(this,
                if (c.esApto) R.color.success_color else R.color.error_color)
        )
    }

    private fun mostrarCarnet(c: Cliente, p: Pago) {
        tvNombreCarnet.text = "${c.nombre} ${c.apellido}"
        tvEstadoSocio.text = "No socio"

        if (c.esSocio) {
            val hoy = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            val vencimiento = try {
                formatter.parse(p.periodoFin ?: "")
            } catch (e: Exception) {
                null
            }

            if (vencimiento != null && vencimiento.before(hoy)) {
                tvEstadoSocio.text = "Carnet vencido"
                tvEstadoSocio.setTextColor(ContextCompat.getColor(this, R.color.error_color))
            } else {
                tvEstadoSocio.text = "Socio activo"
                tvEstadoSocio.setTextColor(ContextCompat.getColor(this, R.color.success_color))
            }

            tvEstadoSocio.visibility = View.VISIBLE
        }

        val qrData = buildString {
            appendLine("Nombre: ${c.nombre}")
            appendLine("Apellido: ${c.apellido}")
            appendLine("DNI: ${c.dni}")
            appendLine("Email: ${c.email}")
            appendLine("Teléfono: ${c.telefono}")
            appendLine("Apto físico: ${if (c.esApto) "Sí" else "No"}")
            if (c.esSocio) {
                appendLine("Inicio: ${p.periodoInicio}")
                appendLine("Fin: ${p.periodoFin}")
            } else {
                appendLine("Fecha de actividad: ${p.periodoInicio}")
            }
        }

        generarQR(qrData)?.let {
            ivCodigoQR.setImageBitmap(it)
        }

        cardCarnet.visibility = View.VISIBLE
        layoutBotones.visibility = View.VISIBLE
    }

    private fun generarQR(texto: String): Bitmap? {
        return try {
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(texto, BarcodeFormat.QR_CODE, 400, 400)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun compartirCarnet() {
        try {
            val cardView = findViewById<androidx.cardview.widget.CardView>(R.id.cardCarnet)

            val targetWidth = resources.displayMetrics.widthPixels - 32.dpToPx()
            val targetHeight = 600.dpToPx()

            cardView.measure(
                View.MeasureSpec.makeMeasureSpec(targetWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(targetHeight, View.MeasureSpec.AT_MOST)
            )
            cardView.layout(0, 0, cardView.measuredWidth, cardView.measuredHeight)

            val topMarginPx = 16.dpToPx()
            val bitmap = Bitmap.createBitmap(
                cardView.measuredWidth,
                cardView.measuredHeight + topMarginPx,
                Bitmap.Config.ARGB_8888
            ).apply {
                val canvas = Canvas(this)
                canvas.drawColor(Color.WHITE)
                canvas.translate(0f, topMarginPx.toFloat())
                cardView.draw(canvas)
            }

            val file = createTempImageFile()
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            shareImageFile(file)

        } catch (e: Exception) {
            Toast.makeText(this, "Error al compartir carnet: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun createTempImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("CARNET_${timeStamp}_", ".png", storageDir)
    }

    private fun shareImageFile(file: File) {
        val contentUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)

        Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }.also { intent ->
            startActivity(Intent.createChooser(intent, "Compartir carnet"))
        }
    }

    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}