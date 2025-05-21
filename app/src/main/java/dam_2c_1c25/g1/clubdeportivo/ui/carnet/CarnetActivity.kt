package dam_2c_1c25.g1.clubdeportivo.ui.carnet

import android.os.Bundle
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity

class CarnetActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_carnet)
        // Cambia el título del toolbar
        supportActionBar?.title = "Carnet"
    }
}