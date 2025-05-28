package dam_2c_1c25.g1.clubdeportivo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cliente(
    val id: Int = 0,
    val nombre: String = "",
    val apellido: String = "",
    val dni: String = "",
    val email: String = "",
    val telefono: String = "",
    val fechaNac: String = "",
    val esSocio: Boolean = false,
    val esApto: Boolean = false
) : Parcelable