package dam_2c_1c25.g1.clubdeportivo.ui.pagoactividades

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.model.Actividad

class ActividadPagoAdapter(
    private val actividades: List<Actividad>,
    private val onActividadSelected: (Actividad, Boolean) -> Unit
) : RecyclerView.Adapter<ActividadPagoAdapter.ActividadViewHolder>() {

    private val seleccionadas = mutableSetOf<Int>()
    private var lastSelectedPosition = -1

    inner class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreActividad)
        private val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioActividad)

        fun bind(actividad: Actividad) {
            tvNombre.text = actividad.nombre
            tvPrecio.text = "$${"%.2f".format(actividad.precio)}"

            // Cambiar color de fondo según selección
            itemView.setBackgroundColor(
                if (seleccionadas.contains(actividad.id))
                    ContextCompat.getColor(itemView.context, R.color.selected_color)
                else
                    ContextCompat.getColor(itemView.context, android.R.color.transparent)
            )

            itemView.setOnClickListener {
                val isSelected = !seleccionadas.contains(actividad.id)
                if (isSelected) {
                    seleccionadas.add(actividad.id)
                } else {
                    seleccionadas.remove(actividad.id)
                }
                notifyItemChanged(adapterPosition)
                onActividadSelected(actividad, isSelected)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad_pago, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        holder.bind(actividades[position])
    }

    override fun getItemCount(): Int = actividades.size

    fun clearSelections() {
        seleccionadas.clear()
        notifyDataSetChanged()
    }
}