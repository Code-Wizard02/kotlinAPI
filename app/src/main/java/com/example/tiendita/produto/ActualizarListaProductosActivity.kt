package com.example.tiendita.produto

import android.content.Intent
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

class ActualizarListaProductosActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private var productos: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_lista_productos)

        recyclerView = findViewById(R.id.recyclerActualizarProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configuramos el adaptador
        adapter = ProductoAdapter(productos) { producto ->
            val intent = Intent(this@ActualizarListaProductosActivity, FormularioActualizarProductoActivity::class.java)
            intent.putExtra("producto", producto)
            startActivityForResult(intent, ACTUALIZAR_PRODUCTO_REQUEST_CODE) // Iniciar la actividad de actualización
        }
        recyclerView.adapter = adapter

        obtenerProductos()
    }

    private fun obtenerProductos() {
        val apiService = RetrofitClient.getInstance(this)

        apiService.getProductos().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val productosRecibidos = response.body() ?: emptyList()
                    productos.clear() // Limpiar la lista actual
                    productos.addAll(productosRecibidos) // Agregar los productos nuevos
                    adapter.notifyDataSetChanged() // Notificar al adaptador que la lista ha cambiado
                } else {
                    Toast.makeText(this@ActualizarListaProductosActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@ActualizarListaProductosActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Manejar el resultado de la actividad de actualización
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ACTUALIZAR_PRODUCTO_REQUEST_CODE && resultCode == RESULT_OK) {
            val idProductoActualizado = data?.getStringExtra("idProductoActualizado")
            idProductoActualizado?.let {
                // Actualizar el producto en la lista con el ID actualizado
                actualizarProductoEnLista(it)
            }
        }
    }

    private fun actualizarProductoEnLista(idProducto: String) {
        val apiService = RetrofitClient.getInstance(this)

        apiService.getProductoPorId(idProducto).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    val productoActualizado = response.body()
                    productoActualizado?.let {
                        val index = productos.indexOfFirst { prod -> prod.id == idProducto }
                        if (index != -1) {
                            productos[index] = it
                            adapter.notifyItemChanged(index)
                        }
                    }
                } else {
                    Toast.makeText(this@ActualizarListaProductosActivity, "Error al refrescar producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@ActualizarListaProductosActivity, "Fallo de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val ACTUALIZAR_PRODUCTO_REQUEST_CODE = 100 // Código de solicitud para la actividad de actualización
    }
}
