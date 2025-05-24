package dam_2c_1c25.g1.clubdeportivo.ui.veractividades

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dam_2c_1c25.g1.clubdeportivo.R
import dam_2c_1c25.g1.clubdeportivo.data.model.Actividad

class ActividadesAdapter(private val actividades: List<Actividad>) :
    RecyclerView.Adapter<ActividadesAdapter.ActividadViewHolder>() {

    // ViewHolder para cada item
    class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvInstructor: TextView = itemView.findViewById(R.id.tvInstructor)
        val tvHorario: TextView = itemView.findViewById(R.id.tvHorario)
        val tvDuracion: TextView = itemView.findViewById(R.id.tvDuracion)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]
        holder.tvNombre.text = actividad.nombre
        holder.tvInstructor.text = "Instructor: ${actividad.instructor}"
        holder.tvHorario.text = "${actividad.dias} | ${actividad.horario}"
        holder.tvDuracion.text = "Duración: ${actividad.duracion}"
        holder.tvPrecio.text = "Precio: $${"%.2f".format(actividad.precio)}"
    }

    override fun getItemCount(): Int = actividades.size
}