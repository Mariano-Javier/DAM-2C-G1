package dam_2c_1c25.g1.clubdeportivo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Actividad(
    val id: Int = 0,
    val nombre: String,
    val dias: String,
    val horario: String,
    val instructor: String,
    val duracion: String,
    val precio: Double
) : Parcelable