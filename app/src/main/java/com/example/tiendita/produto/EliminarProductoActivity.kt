package com.example.tiendita.produto


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendita.R
import com.example.tiendita.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EliminarProductoActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private var productos = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_producto)

        recyclerView = findViewById(R.id.recyclerEliminarProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(productos) { producto ->
            eliminarProductoConConfirmacion(producto)
        }

        recyclerView.adapter = adapter
        obtenerProductos()
    }

    private fun obtenerProductos() {
        RetrofitClient.getInstance(this).getProductos().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    productos.clear()
                    productos.addAll(response.body() ?: emptyList())
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@EliminarProductoActivity, "Fallo al cargar productos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarProductoConConfirmacion(producto: Product) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmar eliminación")
        builder.setMessage("¿Estás seguro de que deseas eliminar el producto \"${producto.nombre}\"?")

        builder.setPositiveButton("Sí") { _, _ ->
            eliminarProducto(producto)
        }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun eliminarProducto(producto: Product) {
        RetrofitClient.getInstance(this).eliminarProducto(producto.id)
            .enqueue(object : Callback<DeleteResponse> {
                override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EliminarProductoActivity, "Producto eliminado", Toast.LENGTH_SHORT).show()
                        productos.remove(producto)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@EliminarProductoActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                    Toast.makeText(this@EliminarProductoActivity, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}