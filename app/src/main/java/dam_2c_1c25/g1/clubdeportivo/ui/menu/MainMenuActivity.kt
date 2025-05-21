package dam_2c_1c25.g1.clubdeportivo.ui.menu

import android.os.Bundle
import android.content.Intent
import com.google.android.material.card.MaterialCardView
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.ui.base.BaseActivity

class MainMenuActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_main_menu)

        // Se vinculan los botones con las Activities
        findViewById<MaterialCardView>(R.id.cardButtonRegistrar).setOnClickListener {
            startActivity(Intent(this, dam_2c_1c25.g1.clubdeportivo.ui.registrarcliente.RegistrarClienteActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardButtonActividades).setOnClickListener {
            startActivity(Intent(this, dam_2c_1c25.g1.clubdeportivo.ui.veractividades.VerActividadesActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardButtonCarnet).setOnClickListener {
            startActivity(Intent(this, dam_2c_1c25.g1.clubdeportivo.ui.carnet.CarnetActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardButtonPagoActividades).setOnClickListener {
            startActivity(Intent(this, dam_2c_1c25.g1.clubdeportivo.ui.pagoactividades.PagoActividadesActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardButtonPagoCuota).setOnClickListener {
            startActivity(Intent(this, dam_2c_1c25.g1.clubdeportivo.ui.cuotasocio.CuotaSocioActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardButtonVencimientos).setOnClickListener {
            startActivity(Intent(this, dam_2c_1c25.g1.clubdeportivo.ui.listarsocios.ListarSociosActivity::class.java))
        }
    }
}