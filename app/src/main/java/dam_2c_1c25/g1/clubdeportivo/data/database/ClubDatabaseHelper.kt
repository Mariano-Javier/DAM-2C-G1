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

        db.execSQL("""
        CREATE TABLE actividad (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            dias TEXT NOT NULL,
            horario TEXT NOT NULL,
            instructor TEXT NOT NULL,
            duracion TEXT NOT NULL,
            precio REAL NOT NULL
        )
    """)

        // Insertar empleado de prueba en tabla empleado
        db.execSQL(
            """
            INSERT INTO empleado (nombre, apellido, dni, email, telefono, fecha_nac, rol, usuario, contrasenia)
            VALUES ('Club', 'Deportivo', '12345678', 'grupo1@clubdeportivo.com', '12222333', '2025-05-02', 'administrador', 'admin', '1234')
            """
        )
// Insertar actividades de prueba en tabla actividad
        db.execSQL("""
    INSERT INTO actividad (nombre, dias, horario, instructor, duracion, precio)
    VALUES 
        ('Fútbol', 'Lunes, Miércoles, Viernes', '18:00 - 19:30', 'Martín Rojas', '90 minutos', 7500.00),
        ('Voley', 'Martes, Jueves', '17:00 - 18:30', 'Carolina Herrera', '90 minutos', 5000.00),
        ('Natación', 'Lunes a Viernes', '07:00 - 08:00', 'Alejandro Castro', '60 minutos', 9000.00),
        ('Gimnasio', 'Lunes a Sábado', '06:00 - 22:00', 'Laura Méndez', '45 minutos', 6500.00),
        ('Pilates', 'Martes, Jueves, Sábado', '09:00 - 10:00', 'Sofía Ramírez', '60 minutos', 7000.00),
        ('Futsal', 'Miércoles, Viernes', '20:00 - 21:30', 'Diego Morales', '90 minutos', 7250.00),
        ('Basket', 'Lunes, Miércoles', '19:00 - 20:30', 'Javier López', '90 minutos', 6900.00),
        ('Tenis', 'Martes, Jueves, Sábado', '08:00 - 09:30', 'Lucía Fernández', '90 minutos', 6700.00),
        ('Acquagym', 'Lunes, Miércoles, Viernes', '10:00 - 11:00', 'Marina Delgado', '60 minutos', 9300.00),
        ('Nutrición', 'Miércoles, Sábado', '11:00 - 12:00', 'Ricardo Soto', '60 minutos', 5500.00)
""")


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS empleado")
        db.execSQL("DROP TABLE IF EXISTS cliente")
        onCreate(db)
    }

}