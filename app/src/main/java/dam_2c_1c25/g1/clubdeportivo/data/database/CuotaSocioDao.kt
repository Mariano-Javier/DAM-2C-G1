package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import dam_2c_1c25.g1.clubdeportivo.data.model.CuotaSocio

class CuotaSocioDao(context: Context) {
    private val dbHelper = ClubDatabaseHelper(context)

    fun obtenerTodasCuotas(): List<CuotaSocio> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM cuota_socio", null)

        val cuotas = mutableListOf<CuotaSocio>()
        with(cursor) {
            while (moveToNext()) {
                cuotas.add(
                    CuotaSocio(
                        id = getInt(getColumnIndexOrThrow("id")),
                        descripcion = getString(getColumnIndexOrThrow("descripcion")),
                        monto = getDouble(getColumnIndexOrThrow("monto"))
                    )
                )
            }
            close()
        }
        db.close()
        return cuotas
    }

    fun obtenerCuotaPorId(id: Int): CuotaSocio? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cuota_socio WHERE id = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            CuotaSocio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto"))
            )
        } else {
            null
        }.also {
            cursor.close()
            db.close()
        }
    }
}