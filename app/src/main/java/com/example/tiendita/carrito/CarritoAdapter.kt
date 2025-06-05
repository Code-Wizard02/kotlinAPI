import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendita.R
import com.example.tiendita.carrito.AddToCartRequest
import com.example.tiendita.carrito.CarritoItem
import com.example.tiendita.carrito.MessageResponse
import com.example.tiendita.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarritoAdapter(
    private val context: Context,
    private val items: MutableList<CarritoItem>,
    private val onCantidadChanged: (pos: Int, nuevaCantidad: Int) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    private val apiService = RetrofitClient.getInstance(context)

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
        holder.subtotal.text = "Subtotal: $${item.producto.precio * item.cantidad}"

        // Botón agregar
        holder.btnAgregar.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val currentItem = items[pos]
                if (currentItem.cantidad < currentItem.producto.stock) {
                    // Llamada a la API para agregar
                    apiService.agregarProductoAlCarrito(
                        AddToCartRequest(productId = currentItem.id, cantidad = 1)
                    ).enqueue(object : Callback<MessageResponse> {
                        override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                            if (response.isSuccessful) {
                                currentItem.cantidad++
                                notifyItemChanged(pos)
                                onCantidadChanged(pos, currentItem.cantidad)
                            } else {
                                Toast.makeText(context, "Error al agregar", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                            Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(context, "No hay más stock disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botón quitar
        holder.btnQuitar.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val currentItem = items[pos]
                apiService.eliminarProductoDelCarrito(currentItem.producto.id)
                    .enqueue(object : Callback<MessageResponse> {
                        override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                            if (response.isSuccessful) {
                                if (currentItem.cantidad > 1) {
                                    currentItem.cantidad--
                                    notifyItemChanged(pos)
                                    onCantidadChanged(pos, currentItem.cantidad)
                                } else {
                                    items.removeAt(pos)
                                    notifyItemRemoved(pos)
                                    onCantidadChanged(pos, 0)
                                }
                            } else {
                                Toast.makeText(context, "Error al quitar producto", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
