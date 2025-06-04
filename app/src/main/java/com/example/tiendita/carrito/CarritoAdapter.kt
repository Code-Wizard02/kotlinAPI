package com.example.tiendita.carrito

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarritoAdapter(private val carrito: List<CarritoItem>) :
    RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout) {
        val nombre: TextView = layout.getChildAt(0) as TextView
        val detalle: TextView = layout.getChildAt(1) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val context = parent.context

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val nombre = TextView(context).apply {
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
        }

        val detalle = TextView(context).apply {
            textSize = 14f
        }

        layout.addView(nombre)
        layout.addView(detalle)

        return CarritoViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = carrito[position]
        holder.nombre.text = item.producto.nombre
        holder.detalle.text = "Cantidad: ${item.cantidad} x $${"%.2f".format(item.producto.precio)}"
    }

    override fun getItemCount(): Int = carrito.size
}
