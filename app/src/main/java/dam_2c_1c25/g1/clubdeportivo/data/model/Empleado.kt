package dam_2c_1c25.g1.clubdeportivo.data.model

import java.io.Serializable

data class Empleado(
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val email: String,
    val telefono: String,
    val fechaNac: String,
    val rol: String,
    val usuario: String,
    val contrasenia: String
) : Serializable {
    companion object {
        var empleadoLogueado: Empleado? = null
    }
}