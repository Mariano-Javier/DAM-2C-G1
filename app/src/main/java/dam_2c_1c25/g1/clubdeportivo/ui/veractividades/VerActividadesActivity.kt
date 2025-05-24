package dam_2c_1c25.g1.clubdeportivo.ui.veractividades

import android.os.Bundle
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dam_2c_1c25.g1.clubdeportivo.data.database.ActividadDao


class VerActividadesActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_ver_actividades)
        // Cambia el título del toolbar
        supportActionBar?.title = "Actividades"

        // Obtener datos de la base de datos
        val actividadDao = ActividadDao(this)
        val actividades = actividadDao.obtenerTodasActividades()

        // Configurar RecyclerView
        val rvActividades = findViewById<RecyclerView>(R.id.rvActividades)
        rvActividades.layoutManager = LinearLayoutManager(this)
        rvActividades.adapter = ActividadesAdapter(actividades)

    }
}