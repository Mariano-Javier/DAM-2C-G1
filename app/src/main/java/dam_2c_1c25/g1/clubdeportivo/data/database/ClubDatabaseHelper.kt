package dam_2c_1c25.g1.clubdeportivo.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClubDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "club_deportivo.db"
        private const val DATABASE_VERSION = 1
    }

    //Creo las tablas
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE empleado (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                apellido TEXT,
                dni TEXT,
                email TEXT,
                telefono TEXT,
                fecha_nac TEXT,
                rol TEXT,
                usuario TEXT,
                contrasenia TEXT
            )
            """
        )

        db.execSQL(
            """
        CREATE TABLE cliente (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            apellido TEXT NOT NULL,
            dni TEXT NOT NULL,
            email TEXT NOT NULL,
            telefono TEXT NOT NULL,
            fecha_nac TEXT NOT NULL,
            es_socio INTEGER NOT NULL,
            es_apto INTEGER NOT NULL
    )
    """
        )

        // Insertar empleado de prueba
        db.execSQL(
            """
            INSERT INTO empleado (nombre, apellido, dni, email, telefono, fecha_nac, rol, usuario, contrasenia)
            VALUES ('Club', 'Deportivo', '12345678', 'grupo1@clubdeportivo.com', '12222333', '2025-05-02', 'administrador', 'admin', '1234')
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS empleado")
        db.execSQL("DROP TABLE IF EXISTS cliente")
        onCreate(db)
    }

}