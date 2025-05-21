package dam_2c_1c25.g1.clubdeportivo.ui.listarsocios

import android.os.Bundle
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity

class ListarSociosActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_listar_socios)
        // Cambia el título del toolbar
        supportActionBar?.title = "Próximos vencimientos"
    }
}