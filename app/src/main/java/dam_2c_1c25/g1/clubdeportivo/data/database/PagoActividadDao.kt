package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.ContentValues
import android.content.Context
import dam_2c_1c25.g1.clubdeportivo.data.model.Pago

class PagoActividadDao(context: Context) : PagoDao(context) {

    fun registrarPagoActividad(
        idCliente: Int,
        monto: Double,
        medioPago: String,
        fechaPago: String,
        periodoInicio: String
    ): Long {
        val values = ContentValues().apply {
            put("id_cliente", idCliente)
            put("monto", monto)
            put("medio_de_pago", medioPago)
            put("fecha_pago", fechaPago)
            put("periodo_inicio", periodoInicio)
            // periodo_fin se deja como NULL para pagos de actividades
            put("socio_al_pagar", 0) // Siempre 0 (false) para pagos de actividades
            // id_cuota se deja como NULL para pagos de actividades
        }

        return insertPago(values)
    }

    // Métodos específicos para pagos de actividades
    fun obtenerPagosActividadPorCliente(idCliente: Int): List<Pago> {
        return obtenerPagosPorCliente(idCliente).filter { !it.socioAlPagar }
    }
}