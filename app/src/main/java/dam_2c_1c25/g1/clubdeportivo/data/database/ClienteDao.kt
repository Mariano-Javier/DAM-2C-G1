package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.ContentValues
import android.content.Context
import dam_2c_1c25.g1.clubdeportivo.data.model.Cliente

class ClienteDao(context: Context) {
    private val dbHelper = ClubDatabaseHelper(context)

    fun insertarCliente(cliente: Cliente): Long {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", cliente.nombre)
            put("apellido", cliente.apellido)
            put("dni", cliente.dni)
            put("email", cliente.email)
            put("telefono", cliente.telefono)
            put("fecha_nac", cliente.fechaNac)
            put("es_socio", if (cliente.esSocio) 1 else 0)
            put("es_apto", if (cliente.esApto) 1 else 0)
        }
        val id = db.insert("cliente", null, valores)
        db.close()
        return id
    }
    fun obtenerClientePorDni(dni: String): Cliente? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cliente WHERE dni = ?", arrayOf(dni))

        return if (cursor.moveToFirst()) {
            Cliente(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                fechaNac = cursor.getString(cursor.getColumnIndexOrThrow("fecha_nac")),
                esSocio = cursor.getInt(cursor.getColumnIndexOrThrow("es_socio")) == 1,
                esApto = cursor.getInt(cursor.getColumnIndexOrThrow("es_apto")) == 1
            )
        } else {
            null
        }.also {
            cursor.close()
            db.close()
        }
    }

    fun obtenerClientePorId(id: Int): Cliente? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cliente WHERE id = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            Cliente(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                fechaNac = cursor.getString(cursor.getColumnIndexOrThrow("fecha_nac")),
                esSocio = cursor.getInt(cursor.getColumnIndexOrThrow("es_socio")) == 1,
                esApto = cursor.getInt(cursor.getColumnIndexOrThrow("es_apto")) == 1
            )
        } else {
            null
        }.also {
            cursor.close()
            db.close()
        }
    }
}
