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