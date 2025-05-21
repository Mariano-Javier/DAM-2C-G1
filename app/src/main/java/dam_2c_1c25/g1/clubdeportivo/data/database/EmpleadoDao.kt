package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.ContentValues
import android.content.Context
import dam_2c_1c25.g1.clubdeportivo.data.model.Empleado

class EmpleadoDao(context: Context) {
    private val dbHelper = ClubDatabaseHelper(context)

    //Verifica el empleado con la base de datos
    fun autenticar(usuario: String, contrasenia: String): Empleado? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM empleado WHERE usuario = ? AND contrasenia = ?",
            arrayOf(usuario, contrasenia)
        )

        var empleado: Empleado? = null
        if (cursor.moveToFirst()) {
            empleado = Empleado(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                fechaNac = cursor.getString(cursor.getColumnIndexOrThrow("fecha_nac")),
                rol = cursor.getString(cursor.getColumnIndexOrThrow("rol")),
                usuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario")),
                contrasenia = cursor.getString(cursor.getColumnIndexOrThrow("contrasenia"))
            )
        }

        cursor.close()
        db.close()
        return empleado
    }
    //Verifica si el usuario del empleado ya existe
    fun existeUsuario(usuario: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM empleado WHERE usuario = ?",
            arrayOf(usuario)
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        db.close()
        return existe
    }
    //Inserta un nuevo empleado
    fun insertarEmpleado(empleado: Empleado): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", empleado.nombre)
            put("apellido", empleado.apellido)
            put("dni", empleado.dni)
            put("email", empleado.email)
            put("telefono", empleado.telefono)
            put("fecha_nac", empleado.fechaNac)
            put("rol", empleado.rol)
            put("usuario", empleado.usuario)
            put("contrasenia", empleado.contrasenia)
        }

        val result = db.insert("empleado", null, values)
        db.close()
        return result != -1L
    }
    // Validar empleado en el login
    fun validarEmpleado(usuario: String, contrasenia: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM empleado WHERE usuario = ? AND contrasenia = ?",
            arrayOf(usuario, contrasenia)
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        db.close()
        return existe
    }
}