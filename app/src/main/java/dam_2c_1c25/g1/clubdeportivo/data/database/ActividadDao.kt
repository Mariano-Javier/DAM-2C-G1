package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import dam_2c_1c25.g1.clubdeportivo.data.model.Actividad

class ActividadDao(context: Context) {
    private val dbHelper = ClubDatabaseHelper(context)

    fun insertarActividad(actividad: Actividad): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", actividad.nombre)
            put("dias", actividad.dias)
            put("horario", actividad.horario)
            put("instructor", actividad.instructor)
            put("duracion", actividad.duracion)
            put("precio", actividad.precio)
        }
        return db.insert("actividad", null, values)
    }

    fun obtenerTodasActividades(): List<Actividad> {
        val actividades = mutableListOf<Actividad>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM actividad", null)

        with(cursor) {
            while (moveToNext()) {
                actividades.add(
                    Actividad(
                        id = getInt(getColumnIndexOrThrow("id")),
                        nombre = getString(getColumnIndexOrThrow("nombre")),
                        dias = getString(getColumnIndexOrThrow("dias")),
                        horario = getString(getColumnIndexOrThrow("horario")),
                        instructor = getString(getColumnIndexOrThrow("instructor")),
                        duracion = getString(getColumnIndexOrThrow("duracion")),
                        precio = getDouble(getColumnIndexOrThrow("precio"))
                    )
                )
            }
            close()
        }
        return actividades
    }
}