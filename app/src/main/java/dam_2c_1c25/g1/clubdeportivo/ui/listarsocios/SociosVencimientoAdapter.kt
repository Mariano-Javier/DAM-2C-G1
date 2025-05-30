package dam_2c_1c25.g1.clubdeportivo.ui.listarsocios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.model.Cliente
import dam_2c_1c25.g1.clubdeportivo.data.model.Pago

class SociosVencimientoAdapter(private val sociosConVencimiento: List<Pair<Cliente, Pago>>) :
    RecyclerView.Adapter<SociosVencimientoAdapter.SocioVencimientoViewHolder>() {

    class SocioVencimientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreCompleto: TextView = itemView.findViewById(R.id.tvNombreCompleto)
        val tvDni: TextView = itemView.findViewById(R.id.tvDni)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
        val tvVencimiento: TextView = itemView.findViewById(R.id.tvVencimiento)
        val tvCuota: TextView = itemView.findViewById(R.id.tvCuota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioVencimientoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio_vencimiento, parent, false)
        return SocioVencimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocioVencimientoViewHolder, position: Int) {
        val (cliente, pago) = sociosConVencimiento[position]

        holder.tvNombreCompleto.text = "${cliente.nombre} ${cliente.apellido}"
        holder.tvDni.text = "DNI: ${cliente.dni}"
        holder.tvTelefono.text = "Teléfono: ${cliente.telefono}"
        holder.tvVencimiento.text = "Vencimiento: ${pago.periodoFin}"
        holder.tvCuota.text = "Cuota: $${"%.2f".format(pago.monto)}"
    }

    override fun getItemCount(): Int = sociosConVencimiento.size
}