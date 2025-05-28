package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.Context
import android.database.Cursor
import dam_2c_1c25.g1.clubdeportivo.data.model.Descuento

class DescuentoDao(context: Context) {
    private val dbHelper = ClubDatabaseHelper(context)

    fun obtenerTodosDescuentos(): List<Descuento> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM descuentos", null)

        val descuentos = mutableListOf<Descuento>()

        with(cursor) {
            while (moveToNext()) {
                descuentos.add(
                    Descuento(
                        id = getInt(getColumnIndexOrThrow("id")),
                        tipoPago = getString(getColumnIndexOrThrow("tipo_pago")),
                        valorDescuento = getDouble(getColumnIndexOrThrow("valor_descuento"))
                    )
                )
            }
            close()
        }
        db.close()
        return descuentos
    }
}