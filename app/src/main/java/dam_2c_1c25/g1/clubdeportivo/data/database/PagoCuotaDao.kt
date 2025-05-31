package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.ContentValues
import android.content.Context
import dam_2c_1c25.g1.clubdeportivo.data.model.Cliente
import dam_2c_1c25.g1.clubdeportivo.data.model.Pago

class PagoCuotaDao(context: Context) : PagoDao(context) {

    fun registrarPagoCuota(
        idCliente: Int,
        monto: Double,
        medioPago: String,
        fechaPago: String,
        periodoInicio: String,
        periodoFin: String,
        idCuota: Int
    ): Long {
        val values = ContentValues().apply {
            put("id_cliente", idCliente)
            put("monto", monto)
            put("medio_de_pago", medioPago)
            put("fecha_pago", fechaPago)
            put("periodo_inicio", periodoInicio)
            put("periodo_fin", periodoFin)
            put("socio_al_pagar", 1) // Siempre 1 (true) para pagos de cuota
            put("id_cuota", idCuota)
        }

        return insertPago(values)
    }

    fun tienePagoActivo(idCliente: Int, fechaInicio: String, fechaFin: String): Boolean {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT COUNT(*) 
        FROM pago 
        WHERE id_cliente = ? 
        AND socio_al_pagar = 1
        AND (
            (date(?) BETWEEN date(periodo_inicio) AND date(periodo_fin)) OR
            (date(?) BETWEEN date(periodo_inicio) AND date(periodo_fin)) OR
            (date(periodo_inicio) BETWEEN date(?) AND date(?)) OR
            (date(periodo_fin) BETWEEN date(?) AND date(?))
        )
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(
            idCliente.toString(),
            fechaInicio, fechaFin,
            fechaInicio, fechaFin,
            fechaInicio, fechaFin
        ))

        val count = if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            0
        }

        cursor.close()
        db.close()

        return count > 0
    }

    fun obtenerFechaFinPagoActivo(idCliente: Int): String? {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT MAX(date(periodo_fin)) 
        FROM pago 
        WHERE id_cliente = ? 
        AND socio_al_pagar = 1
        AND date(periodo_fin) >= date('now')
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(idCliente.toString()))

        var fechaFin: String? = null
        try {
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                fechaFin = cursor.getString(0)
            }
        } finally {
            cursor.close()
            db.close()
        }

        return fechaFin
    }

    // Métodos específicos para pagos de cuota
    fun obtenerPagosCuotaPorCliente(idCliente: Int): List<Pago> {
        return obtenerPagosPorCliente(idCliente).filter { it.socioAlPagar }
    }

    fun obtenerSociosConVencimiento(fechaVencimiento: String): List<Pair<Cliente, Pago>> {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT c.*, p.* 
        FROM cliente c 
        JOIN pago p ON c.id = p.id_cliente 
        WHERE c.es_socio = 1 
        AND p.periodo_fin = ?
        AND p.socio_al_pagar = 1
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(fechaVencimiento))

        val resultados = mutableListOf<Pair<Cliente, Pago>>()

        with(cursor) {
            while (moveToNext()) {
                val cliente = Cliente(
                    id = getInt(getColumnIndexOrThrow("id")),
                    nombre = getString(getColumnIndexOrThrow("nombre")),
                    apellido = getString(getColumnIndexOrThrow("apellido")),
                    dni = getString(getColumnIndexOrThrow("dni")),
                    email = getString(getColumnIndexOrThrow("email")),
                    telefono = getString(getColumnIndexOrThrow("telefono")),
                    fechaNac = getString(getColumnIndexOrThrow("fecha_nac")),
                    esSocio = getInt(getColumnIndexOrThrow("es_socio")) == 1,
                    esApto = getInt(getColumnIndexOrThrow("es_apto")) == 1
                )

                val pago = Pago(
                    id = getInt(getColumnIndexOrThrow("id")),
                    idCliente = getInt(getColumnIndexOrThrow("id_cliente")),
                    monto = getDouble(getColumnIndexOrThrow("monto")),
                    medioDePago = getString(getColumnIndexOrThrow("medio_de_pago")),
                    fechaPago = getString(getColumnIndexOrThrow("fecha_pago")),
                    periodoInicio = getString(getColumnIndexOrThrow("periodo_inicio")),
                    periodoFin = getString(getColumnIndexOrThrow("periodo_fin")),
                    socioAlPagar = getInt(getColumnIndexOrThrow("socio_al_pagar")) == 1,
                    idCuota = if (isNull(getColumnIndexOrThrow("id_cuota"))) null else getInt(getColumnIndexOrThrow("id_cuota"))
                )

                resultados.add(Pair(cliente, pago))
            }
            close()
        }
        db.close()
        return resultados
    }
}