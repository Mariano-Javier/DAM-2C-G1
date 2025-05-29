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

        // tabla cuota_socio
        db.execSQL("""
    CREATE TABLE cuota_socio (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        descripcion TEXT NOT NULL,
        monto REAL NOT NULL
    )
""")

        // tabla descuentos
        db.execSQL("""
    CREATE TABLE descuentos (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        tipo_pago TEXT NOT NULL,
        valor_descuento REAL NOT NULL
    )
""")

        // Crear tabla pago
        db.execSQL("""
    CREATE TABLE pago (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        id_cliente INTEGER NOT NULL,
        monto REAL NOT NULL,
        medio_de_pago TEXT NOT NULL,
        fecha_pago TEXT NOT NULL,
        periodo_inicio TEXT NOT NULL,
        periodo_fin TEXT,
        socio_al_pagar INTEGER NOT NULL,
        id_cuota INTEGER,
        FOREIGN KEY (id_cliente) REFERENCES cliente(id),
        FOREIGN KEY (id_cuota) REFERENCES cuota_socio(id)
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

        // Insertar clientes de prueba en tabla cliente
        db.execSQL("""
    INSERT INTO cliente (nombre, apellido, dni, email, telefono, fecha_nac, es_socio, es_apto)
    VALUES 
        ('María', 'González', '12345678A', 'maria.gonzalez@example.com', '611223344', '1990-05-15', 1, 1),
        ('Ana', 'Martínez', '34567890C', 'ana.martinez@example.com', '633445566', '1992-07-22', 1, 0),
        ('Juan', 'Sánchez', '45678901D', 'juan.sanchez@example.com', '644556677', '1980-03-11', 1, 1),
        ('Carlos', 'García', '67890123F', 'carlos.garcia@example.com', '666778899', '1987-09-30', 1, 0),
        ('Miguel', 'Pérez', '89012345H', 'miguel.perez@example.com', '688990011', '1991-06-27', 1, 1),
        ('David', 'Ramírez', '01234567J', 'david.ramirez@example.com', '600112233', '1989-01-05', 1, 1),
        ('Sara', 'Flores', '11122334K', 'sara.flores@example.com', '611223344', '1996-04-19', 1, 0),
        ('Isabel', 'Díaz', '33344556M', 'isabel.diaz@example.com', '633445566', '1988-11-07', 1, 0),
        ('Patricia', 'Gómez', '55566778O', 'patricia.gomez@example.com', '655667788', '1992-09-05', 1, 1),
        ('Pedro', 'López', '23456789B', 'pedro.lopez@example.com', '622334455', '1985-11-03', 0, 1),
        ('Elena', 'Fernández', '56789012E', 'elena.fernandez@example.com', '655667788', '1995-12-18', 0, 1),
        ('Laura', 'Hernández', '78901234G', 'laura.hernandez@example.com', '677889900', '1993-02-08', 0, 1),
        ('Carmen', 'Rodríguez', '90123456I', 'carmen.rodriguez@example.com', '699001122', '1994-10-14', 0, 0),
        ('Álvaro', 'Ruiz', '22233445L', 'alvaro.ruiz@example.com', '622334455', '1990-08-23', 0, 1),
        ('José', 'Torres', '44455667N', 'jose.torres@example.com', '644556677', '1991-03-15', 0, 1)
""")

        // Insertar datos en cuota_socio
        db.execSQL("""
    INSERT INTO cuota_socio (descripcion, monto)  VALUES 
          ('Mensual', 40000.00),
          ('Anual', 350000.00)
""")

        // Insertar datos en descuentos
        db.execSQL("""
    INSERT INTO descuentos (tipo_pago, valor_descuento) VALUES 
        ('Tarjeta', 0.10),
        ('Transferencia', 0.05),
        ('Efectivo', 0.20)
""")
        // Insertar datos en pago de socios
        db.execSQL("""
    INSERT INTO pago (id_cliente, monto, medio_de_pago, fecha_pago, periodo_inicio, periodo_fin, socio_al_pagar, id_cuota) VALUES
    (1, 35000.00, 'Efectivo', '2024-09-15', '2024-09-15', '2024-10-15', 1, 1),
    (2, 40000.00, 'Tarjeta en 3 cuotas', '2024-08-10', '2024-08-10', '2024-11-10', 1, 1),
    (3, 35000.00, 'Efectivo', '2024-07-05', '2024-07-05', '2024-08-05', 1, 1),
    (4, 40000.00, 'Tarjeta en 6 cuotas', '2024-06-20', '2024-06-20', '2024-12-20', 1, 1),
    (5, 35000.00, 'Efectivo', '2024-05-25', '2024-05-25', '2024-06-25', 1, 1),
    (6, 40000.00, 'Tarjeta en 3 cuotas', '2024-04-30', '2024-04-30', '2024-07-30', 1, 1),
    (7, 35000.00, 'Efectivo', '2024-03-15', '2024-03-15', '2024-04-15', 1, 1),
    (8, 40000.00, 'Tarjeta en 6 cuotas', '2024-02-10', '2024-02-10', '2024-08-10', 1, 1),
    (9, 35000.00, 'Efectivo', '2024-01-05', '2024-01-05', '2024-02-05', 1, 1)
""")
        // Insertar datos en pago de no socios

        db.execSQL("""
    INSERT INTO pago (id_cliente, monto, medio_de_pago, fecha_pago, periodo_inicio, periodo_fin, socio_al_pagar, id_cuota) VALUES
    (10, 5000.00, 'Efectivo', '2024-10-01', '2024-10-01', NULL, 0, NULL),
    (11, 7000.00, 'Tarjeta en 3 cuotas', '2024-09-15', '2024-09-15', NULL, 0, NULL),
    (12, 7500.00, 'Tarjeta en 6 cuotas', '2024-07-30', '2024-07-30', NULL, 0, NULL),
    (13, 8000.00, 'Efectivo', '2024-06-25', '2024-06-25', NULL, 0, NULL),
    (14, 6800.00, 'Tarjeta en 3 cuotas', '2024-05-10', '2024-05-10', NULL, 0, NULL),
    (15, 5200.00, 'Efectivo', '2024-04-18', '2024-04-18', NULL, 0, NULL)
""")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar todas las tablas existentes
        db.execSQL("DROP TABLE IF EXISTS empleado")
        db.execSQL("DROP TABLE IF EXISTS cliente")
        db.execSQL("DROP TABLE IF EXISTS actividad")
        db.execSQL("DROP TABLE IF EXISTS cuota_socio")
        db.execSQL("DROP TABLE IF EXISTS descuentos")
        db.execSQL("DROP TABLE IF EXISTS pago")

        // Volver a crear todas las tablas
        onCreate(db)
    }
}