package dam_2c_1c25.g1.clubdeportivo.ui.listarsocios

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.database.PagoCuotaDao
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListarSociosActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_listar_socios)
        supportActionBar?.title = "Próximos vencimientos"

        // Obtener la fecha actual en formato YYYY-MM-DD
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaActual = dateFormat.format(Date())

        // Obtener socios con vencimientos hoy usando el nuevo método
        val pagoDao = PagoCuotaDao(this)
        val sociosConVencimiento = pagoDao.obtenerSociosConVencimiento(fechaActual)

        // Configurar RecyclerView
        val rvSocios = findViewById<RecyclerView>(R.id.rvSocios)
        val tvEmptyView = findViewById<TextView>(R.id.tvEmptyView)

        if (sociosConVencimiento.isEmpty()) {
            rvSocios.visibility = View.GONE
            tvEmptyView.visibility = View.VISIBLE
        } else {
            rvSocios.visibility = View.VISIBLE
            tvEmptyView.visibility = View.GONE

            rvSocios.layoutManager = LinearLayoutManager(this)
            rvSocios.adapter = SociosVencimientoAdapter(sociosConVencimiento)
        }
    }
}