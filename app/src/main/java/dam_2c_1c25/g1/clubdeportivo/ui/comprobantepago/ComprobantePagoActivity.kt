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

        val actividades = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("actividades", Actividad::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra<Actividad>("actividades")
        }

        val fecha = intent.getStringExtra("fecha") ?: ""
        val metodoPago = intent.getStringExtra("metodoPago") ?: ""
        val subtotal = intent.getDoubleExtra("subtotal", 0.0)
        val descuento = intent.getDoubleExtra("descuento", 0.0)
        val total = intent.getDoubleExtra("total", 0.0)

        // Verificar que los datos necesarios no sean nulos
        if (cliente == null || actividades == null) {
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

        // Configurar vistas de forma segura
        findViewById<TextView>(R.id.tvCliente).text = "Cliente: ${cliente.nombre} ${cliente.apellido}"
        findViewById<TextView>(R.id.tvDni).text = "DNI: ${cliente.dni}"
        findViewById<TextView>(R.id.tvFecha).text = "Fecha: ${fechaFormateada}"
        findViewById<TextView>(R.id.tvMetodoPago).text = "Método de Pago: ${metodoPago}"

        val actividadesText = actividades.joinToString("\n") {
            "• ${it.nombre} - $${"%.2f".format(it.precio)}"
        }
        findViewById<TextView>(R.id.tvActividades).text = actividadesText

        findViewById<TextView>(R.id.tvSubtotal).text = "$${"%.2f".format(subtotal)}"
        findViewById<TextView>(R.id.tvDescuento).text = "-$${"%.2f".format(descuento)}"
        findViewById<TextView>(R.id.tvTotal).text = "$${"%.2f".format(total)}"

        // Configurar botón compartir
        comprobanteView = findViewById(R.id.cardComprobante)
        findViewById<Button>(R.id.btnCompartir).setOnClickListener {
            compartirComprobante()
        }

        findViewById<Button>(R.id.btnFinalizar).setOnClickListener {
            finish()
        }
    }

    private fun compartirComprobante() {
        try {
            val cardView = findViewById<MaterialCardView>(R.id.cardComprobante)

            // Medidas fijas
            val targetWidth = resources.displayMetrics.widthPixels - 32.dpToPx()
            val targetHeight = 600.dpToPx() // O lo que necesites

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




    // Función auxiliar para crear archivo temporal
    private fun createTempImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "COMPROBANTE_${timeStamp}_",
            ".png",
            storageDir
        )
    }

    // Función auxiliar para compartir el archivo
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
    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()


}