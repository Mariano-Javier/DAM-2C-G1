package dam_2c_1c25.g1.clubdeportivo.ui.cuotasocio

import android.os.Bundle
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity

class CuotaSocioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_cuota_socio)
        // Cambia el título del toolbar
        supportActionBar?.title = "Pago de Cuotas"


    }
}