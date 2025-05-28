package dam_2c_1c25.g1.clubdeportivo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Descuento(
    val id: Int = 0,
    val tipoPago: String,
    val valorDescuento: Double
) : Parcelable