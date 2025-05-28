package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.ContentValues
import android.content.Context
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
}