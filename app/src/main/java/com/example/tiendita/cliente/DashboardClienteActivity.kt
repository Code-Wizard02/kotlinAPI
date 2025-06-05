package com.example.tiendita.cliente

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendita.R
import com.example.tiendita.carrito.CarritoActivity
import com.example.tiendita.carrito.CarritoItem
import com.example.tiendita.carrito.MessageResponse
import com.example.tiendita.carrito.AddToCartRequest
import com.example.tiendita.carrito.CartResponse
import com.example.tiendita.data.RetrofitClient
import com.example.tiendita.produto.Product
import com.example.tiendita.produto.ProductoClienteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardClienteActivity : ComponentActivity() {

    private lateinit var recyclerProductos: RecyclerView
    private lateinit var badgeCarrito: TextView
    private val carrito = mutableListOf<CarritoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_cliente)

        badgeCarrito = findViewById(R.id.badgeCarrito)
        val btnCarrito: FloatingActionButton = findViewById(R.id.btnCarrito)
        recyclerProductos = findViewById(R.id.recyclerProductos)

        recyclerProductos.layoutManager = LinearLayoutManager(this)
        recyclerProductos.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        btnCarrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            intent.putExtra("carrito", ArrayList(carrito))
            startActivityForResult(intent, 1001)
        }

        cargarProductos()         // cargar lista de productos primero
        cargarCarritoDesdeAPI()   // luego cargar carrito del backend para sincronizar
    }

    private fun cargarProductos() {
        val apiService = RetrofitClient.getInstance(this)

        apiService.getProductos().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    val productos = response.body()!!
                    recyclerProductos.adapter = ProductoClienteAdapter(productos) { producto ->
                        agregarAlCarrito(producto)
                    }
                } else {
                    Toast.makeText(this@DashboardClienteActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@DashboardClienteActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun agregarAlCarrito(producto: Product) {
        val apiService = RetrofitClient.getInstance(this)
        val itemExistente = carrito.find { it.producto.id == producto.id }
        if (itemExistente != null) {
            if (itemExistente.cantidad < producto.stock) {
                itemExistente.cantidad++
            } else {
                Toast.makeText(this, "No hay más stock disponible", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            if (producto.stock > 0) {
                carrito.add(CarritoItem(producto.id, producto, 1))
            } else {
                Toast.makeText(this, "Producto sin stock", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Enviar al backend
        apiService.agregarProductoAlCarrito(
            AddToCartRequest(productId = producto.id, cantidad = 1)
        ).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DashboardClienteActivity, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                    actualizarBadge()
                } else {
                    Toast.makeText(this@DashboardClienteActivity, "Error al agregar al carrito", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Toast.makeText(this@DashboardClienteActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarBadge() {
        val totalItems = carrito.sumOf { it.cantidad }
        if (totalItems > 0) {
            badgeCarrito.visibility = View.VISIBLE
            badgeCarrito.text = totalItems.toString()
        } else {
            badgeCarrito.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val carritoActualizado = data?.getSerializableExtra("carritoActualizado") as? ArrayList<CarritoItem>
            if (carritoActualizado != null) {
                carrito.clear()
                carrito.addAll(carritoActualizado)
                actualizarBadge()
            }
        }
    }

    private fun cargarCarritoDesdeAPI() {
        val apiService = RetrofitClient.getInstance(this)
        apiService.obtenerCarrito().enqueue(object : Callback<CartResponse> {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                if (response.isSuccessful) {
                    carrito.clear()
                    val carritoApi = response.body()
                    carritoApi?.productos?.forEach { item ->
                        carrito.add(CarritoItem(
                            id = item.producto.id,
                            producto = item.producto,
                            cantidad = item.cantidad
                        ))
                    }
                    actualizarBadge()
                } else {
                    Toast.makeText(this@DashboardClienteActivity, "Error al cargar carrito", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                Toast.makeText(this@DashboardClienteActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
