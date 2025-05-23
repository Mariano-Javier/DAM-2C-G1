package dam_2c_1c25.g1.clubdeportivo.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.model.Empleado
import dam_2c_1c25.g1.clubdeportivo.ui.ayuda.AyudaActivity
import dam_2c_1c25.g1.clubdeportivo.ui.carnet.CarnetActivity
import dam_2c_1c25.g1.clubdeportivo.ui.cuotasocio.CuotaSocioActivity
import dam_2c_1c25.g1.clubdeportivo.ui.listarsocios.ListarSociosActivity
import dam_2c_1c25.g1.clubdeportivo.ui.pagoactividades.PagoActividadesActivity
import dam_2c_1c25.g1.clubdeportivo.ui.registrarcliente.RegistrarClienteActivity
import dam_2c_1c25.g1.clubdeportivo.ui.veractividades.VerActividadesActivity
import dam_2c_1c25.g1.clubdeportivo.ui.login.LoginActivity


open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "Menú Principal"

        val drawerLayout = findViewById<DrawerLayout>(R.id.main)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
    }

    //Setear el nombre y email de empleado en nav_header
    protected fun updateNavHeader() {
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val titleTextView = headerView.findViewById<TextView>(R.id.nav_header_title)
        val subtitleTextView = headerView.findViewById<TextView>(R.id.nav_header_subtitle)

        Empleado.empleadoLogueado?.let {
            titleTextView.text = "${it.nombre} ${it.apellido}"
            subtitleTextView.text = it.email
        } ?: run {
            titleTextView.text = "Club Deportivo Argentino"
            subtitleTextView.text = "usuario@ejemplo.com"
        }
    }

    /** Funciones del botón de volver atras en menú */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_back -> {
                onBackPressedDispatcher.onBackPressed() // o finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Permite cargar contenido específico de cada Activity hija */
    fun setContentLayout(layoutResId: Int) {
        val frameLayout = findViewById<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(layoutResId, frameLayout, true)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_registrar_clientes -> {
                startActivity(Intent(this, RegistrarClienteActivity::class.java))
            }
            R.id.nav_ver_actividades -> {
                startActivity(Intent(this, VerActividadesActivity::class.java))
            }
            R.id.nav_carnet -> {
                startActivity(Intent(this, CarnetActivity::class.java))
            }
            R.id.nav_pago_actividades -> {
                startActivity(Intent(this, PagoActividadesActivity::class.java))
            }
            R.id.nav_cuota_socio -> {
                startActivity(Intent(this, CuotaSocioActivity::class.java))
            }
            R.id.nav_vencimientos -> {
                startActivity(Intent(this, ListarSociosActivity::class.java))
            }
            R.id.nav_ayuda -> {
                startActivity(Intent(this, AyudaActivity::class.java))
            }
            R.id.nav_logout -> {

                // Va a la pantalla de login y cierra la actividad actual
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

        }

        // Evita que quede marcado el ítem en el menu
        item.isChecked = false

        // Cierra el drawer luego de hacer click
        findViewById<DrawerLayout>(R.id.main).closeDrawer(GravityCompat.START)
        return true
    }
}
