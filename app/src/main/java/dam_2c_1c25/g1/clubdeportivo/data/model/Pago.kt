package dam_2c_1c25.g1.clubdeportivo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pago(
    val id: Int = 0,
    val idCliente: Int,
    val monto: Double,
    val medioDePago: String,
    val fechaPago: String,
    val periodoInicio: String,
    val periodoFin: String?,
    val socioAlPagar: Boolean,
    val idCuota: Int?
) : Parcelable