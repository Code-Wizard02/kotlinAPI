package com.example.tiendita.carrito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendita.R

class CarritoAdapter(
    private val items: MutableList<CarritoItem>,
    private val onCantidadChanged: (pos: Int, nuevaCantidad: Int) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.txtNombre)
        val cantidad: TextView = view.findViewById(R.id.txtCantidad)
        val precio: TextView = view.findViewById(R.id.txtPrecio)
        val subtotal: TextView = view.findViewById(R.id.txtSubtotal)
        val btnAgregar: ImageButton = view.findViewById(R.id.btnAgregar)
        val btnQuitar: ImageButton = view.findViewById(R.id.btnQuitar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = items[position]

        holder.nombre.text = item.producto.nombre
        holder.precio.text = "Precio: $${item.producto.precio}"
        holder.cantidad.text = item.cantidad.toString()
        val subtotal = item.cantidad * item.producto.precio
        holder.subtotal.text = "Subtotal: $${subtotal}"

        // Botón agregar
        holder.btnAgregar.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val currentItem = items[pos]
                if (currentItem.cantidad < currentItem.producto.stock) {
                    currentItem.cantidad++
                    notifyItemChanged(pos)
                    onCantidadChanged(pos, currentItem.cantidad)
                }
            }
        }

        // Botón quitar
        holder.btnQuitar.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val currentItem = items[pos]
                if (currentItem.cantidad > 1) {
                    currentItem.cantidad--
                    notifyItemChanged(pos)
                    onCantidadChanged(pos, currentItem.cantidad)
                } else {
                    items.removeAt(pos)
                    notifyItemRemoved(pos)
                    onCantidadChanged(pos, 0)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
