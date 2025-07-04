package com.example.tiendita.produto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendita.R
import android.widget.Toast

class ProductoClienteAdapter (
    private val productos: List<Product>,
    private val onAgregarClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductoClienteAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        val descripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val precio: TextView = itemView.findViewById(R.id.tvPrecio)
        val stock: TextView = itemView.findViewById(R.id.tvStock)
        val btnAgregar: ImageButton = itemView.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto_cliente, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombre.text = producto.nombre
        holder.descripcion.text = producto.descripcion
        holder.precio.text = "Precio: \$${producto.precio}"
        holder.stock.text = "Stock: ${producto.stock}"

        holder.btnAgregar.setOnClickListener {
            onAgregarClick(producto)
        }
    }

    override fun getItemCount(): Int = productos.size
}
