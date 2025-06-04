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
            intent.putExtra("carrito", ArrayList(carrito)) // CarritoItem debe implementar Serializable
            startActivity(intent)
        }

        cargarProductos()
        actualizarBadge()
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
        val itemExistente = carrito.find { it.producto.id == producto.id }
        if (itemExistente != null) {
            if (itemExistente.cantidad < producto.stock) {
                itemExistente.cantidad++
                Toast.makeText(this, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay más stock disponible", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            if (producto.stock > 0) {
                carrito.add(CarritoItem(producto.id, producto, 1))
                Toast.makeText(this, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Producto sin stock", Toast.LENGTH_SHORT).show()
                return
            }
        }
        actualizarBadge()
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
}
