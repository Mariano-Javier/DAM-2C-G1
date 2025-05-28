package dam_2c_1c25.g1.clubdeportivo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CuotaSocio(
    val id: Int = 0,
    val descripcion: String,
    val monto: Double
) : Parcelable