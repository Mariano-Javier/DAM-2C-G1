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
}
