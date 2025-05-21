package dam_2c_1c25.g1.clubdeportivo.data.model

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
)