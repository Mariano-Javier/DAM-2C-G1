package dam_2c_1c25.g1.clubdeportivo.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.database.ClubDatabaseHelper
import dam_2c_1c25.g1.clubdeportivo.data.database.EmpleadoDao
import dam_2c_1c25.g1.clubdeportivo.ui.menu.MainMenuActivity
import dam_2c_1c25.g1.clubdeportivo.ui.registrarempleado.RegistrarEmpleadoActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: ClubDatabaseHelper
    private lateinit var empleadoDao: EmpleadoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = ClubDatabaseHelper(this)
        empleadoDao = EmpleadoDao(this)

         //Valores del layout
        val usuarioEditText = findViewById<TextInputEditText>(R.id.textInputEditUsuario)
        val contraseniaEditText = findViewById<TextInputEditText>(R.id.textInputEditPassword)
        val buttonIngresar = findViewById<Button>(R.id.buttonIngresar)
        val textRegistrarse = findViewById<TextView>(R.id.textRegistrarse)

        //Listener del evento clickear el botón registrar
        buttonIngresar.setOnClickListener {
            val usuario = usuarioEditText.text.toString()
            val contrasenia = contraseniaEditText.text.toString()

            // Uso el método de instancia
            if (empleadoDao.validarEmpleado(usuario, contrasenia)) {
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

         //Listener del evento registrar empleado
        textRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistrarEmpleadoActivity::class.java)
            startActivity(intent)
        }
    }
}
