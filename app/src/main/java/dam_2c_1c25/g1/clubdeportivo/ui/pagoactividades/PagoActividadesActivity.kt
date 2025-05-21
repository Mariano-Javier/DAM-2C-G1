package dam_2c_1c25.g1.clubdeportivo.ui.pagoactividades

import android.os.Bundle
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity

class PagoActividadesActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_pago_actividades)
        // Cambia el título del toolbar
        supportActionBar?.title = "Pago de Actividades"
    }
}