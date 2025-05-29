package dam_2c_1c25.g1.clubdeportivo.ui.comprobantepago

import android.os.Bundle
import android.view.View
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.material.card.MaterialCardView
import dam_2c_1c25.g1.clubdeportivo.data.model.Actividad
import dam_2c_1c25.g1.clubdeportivo.data.model.Cliente
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ComprobantePagoActivity : BaseActivity() {

    private lateinit var comprobanteView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_comprobante_pago)
        supportActionBar?.title = "Comprobante de Pago"

        // Obtener datos del intent de forma segura
        val cliente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("cliente", Cliente::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Cliente>("cliente")
        }

        val fecha = intent.getStringExtra("fecha") ?: ""
        val metodoPago = intent.getStringExtra("metodoPago") ?: ""
        val subtotal = intent.getDoubleExtra("subtotal", 0.0)
        val descuento = intent.getDoubleExtra("descuento", 0.0)
        val total = intent.getDoubleExtra("total", 0.0)

        // Verificar si es pago de cuota (socio) o actividades (no socio)
        val esCuotaSocio = intent.getBooleanExtra("esCuotaSocio", false)

        // Verificar que los datos necesarios no sean nulos
        if (cliente == null) {
            Toast.makeText(this, "Error al cargar los datos del pago", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Formatear fecha para mostrar
        val fechaFormateada = try {
            val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatoSalida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = formatoEntrada.parse(fecha)
            formatoSalida.format(date ?: Date())
        } catch (e: Exception) {
            fecha
        }

        // Configurar vistas comunes
        findViewById<TextView>(R.id.tvCliente).text = "Cliente: ${cliente.nombre} ${cliente.apellido}"
        findViewById<TextView>(R.id.tvDni).text = "DNI: ${cliente.dni}"
        findViewById<TextView>(R.id.tvFecha).text = "Fecha: ${fechaFormateada}"
        findViewById<TextView>(R.id.tvMetodoPago).text = "Método de Pago: ${metodoPago}"

        if (esCuotaSocio) {
            // Configurar vista para pago de cuota (socio)
            configurarParaCuotaSocio(cliente)
        } else {
            // Configurar vista para pago de actividades (no socio)
            configurarParaActividades(cliente)
        }

        // Configurar montos (comunes para ambos tipos)
        findViewById<TextView>(R.id.tvSubtotal).text = "Subtotal: $${"%.2f".format(subtotal)}"
        findViewById<TextView>(R.id.tvDescuento).text = "Descuento: -$${"%.2f".format(descuento)}"
        findViewById<TextView>(R.id.tvTotal).text = "Total: $${"%.2f".format(total)}"

        // Configurar botón compartir
        comprobanteView = findViewById(R.id.cardComprobante)
        findViewById<Button>(R.id.btnCompartir).setOnClickListener {
            compartirComprobante()
        }

        findViewById<Button>(R.id.btnFinalizar).setOnClickListener {
            finish()
        }
    }

    private fun configurarParaActividades(cliente: Cliente) {
        val actividades = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("actividades", Actividad::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra<Actividad>("actividades")
        }

        if (actividades == null || actividades.isEmpty()) {
            Toast.makeText(this, "Error al cargar actividades", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.tvTipoPagoLabel).text = "Actividades:"

        val actividadesText = actividades.joinToString("\n") {
            "• ${it.nombre} - $${"%.2f".format(it.precio)}"
        }
        findViewById<TextView>(R.id.tvDetallesPago).text = actividadesText

        // Ocultar sección de cuota
        findViewById<LinearLayout>(R.id.layoutCuotaInfo).visibility = View.GONE
    }

    private fun configurarParaCuotaSocio(cliente: Cliente) {
        val tipoCuota = intent.getStringExtra("tipoCuota") ?: ""
        val duracion = intent.getIntExtra("duracion", 1)
        val periodoInicio = intent.getStringExtra("periodoInicio") ?: ""
        val periodoFin = intent.getStringExtra("periodoFin") ?: ""

        findViewById<TextView>(R.id.tvTipoPagoLabel).text = "Detalles de la cuota:"

        // Mostrar sección de cuota
        val layoutCuotaInfo = findViewById<LinearLayout>(R.id.layoutCuotaInfo)
        layoutCuotaInfo.visibility = View.VISIBLE

        // Formatear fechas
        val periodoInicioFormateado = try {
            val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatoSalida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatoSalida.format(formatoEntrada.parse(periodoInicio) ?: Date())
        } catch (e: Exception) {
            periodoInicio
        }

        val periodoFinFormateado = try {
            val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatoSalida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatoSalida.format(formatoEntrada.parse(periodoFin) ?: Date())
        } catch (e: Exception) {
            periodoFin
        }

        findViewById<TextView>(R.id.tvTipoCuota).text = "Tipo de cuota: $tipoCuota"
        findViewById<TextView>(R.id.tvDuracion).text = "Duración: $duracion ${if (tipoCuota == "Mensual") "mes(es)" else "año(s)"}"
        findViewById<TextView>(R.id.tvPeriodoInicio).text = "Fecha inicio: $periodoInicioFormateado"
        findViewById<TextView>(R.id.tvPeriodoFin).text = "Fecha vencimiento: $periodoFinFormateado"

        // Ocultar detalles de actividades (ya que se mostraron los de cuota)
        findViewById<TextView>(R.id.tvDetallesPago).visibility = View.GONE
    }

    private fun compartirComprobante() {
        try {
            val cardView = findViewById<MaterialCardView>(R.id.cardComprobante)

            // Medidas fijas
            val targetWidth = resources.displayMetrics.widthPixels - 32.dpToPx()
            val targetHeight = 600.dpToPx() // Aumentado para incluir posible información adicional

            // 1. Medir con dimensiones fijas
            cardView.measure(
                View.MeasureSpec.makeMeasureSpec(targetWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(targetHeight, View.MeasureSpec.EXACTLY)
            )

            // 2. Forzar layout con esas medidas
            cardView.layout(0, 0, cardView.measuredWidth, cardView.measuredHeight)

            // 3. Margen superior (por ejemplo, 16dp)
            val topMarginPx = 16.dpToPx()

            // 4. Crear un bitmap más alto para incluir el margen superior
            val bitmap = Bitmap.createBitmap(
                cardView.measuredWidth,
                cardView.measuredHeight + topMarginPx,
                Bitmap.Config.ARGB_8888
            ).apply {
                val canvas = Canvas(this)
                canvas.drawColor(Color.WHITE)
                canvas.translate(0f, topMarginPx.toFloat()) // Desplazar el dibujo hacia abajo
                cardView.draw(canvas)
            }

            // 5. Guardar imagen temporal y compartir
            val file = createTempImageFile()
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            shareImageFile(file)

        } catch (e: Exception) {
            Toast.makeText(this, "Error al generar comprobante: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun createTempImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "COMPROBANTE_${timeStamp}_",
            ".png",
            storageDir
        )
    }

    private fun shareImageFile(file: File) {
        val contentUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )

        Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }.also { intent ->
            startActivity(Intent.createChooser(intent, "Compartir comprobante"))
        }
    }

    // Extensión para convertir dp a px
    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}