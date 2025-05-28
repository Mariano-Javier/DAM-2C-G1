package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.ContentValues
import android.content.Context
import dam_2c_1c25.g1.clubdeportivo.data.model.Pago

abstract class PagoDao(context: Context) {
    protected val dbHelper = ClubDatabaseHelper(context)

    protected fun insertPago(values: ContentValues): Long {
        val db = dbHelper.writableDatabase
        return db.insert("pago", null, values)
    }

    // Métodos comunes  para ambos tipos de pago
    fun obtenerPagosPorCliente(idCliente: Int): List<Pago> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM pago WHERE id_cliente = ?", arrayOf(idCliente.toString()))

        val pagos = mutableListOf<Pago>()
        with(cursor) {
            while (moveToNext()) {
                pagos.add(
                    Pago(
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
                )
            }
            close()
        }
        return pagos
    }
}