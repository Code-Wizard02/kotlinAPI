package com.example.tiendita

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MostrarProductoActivity : ComponentActivity() {

    private lateinit var recyclerProductos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_producto)

        recyclerProductos = findViewById(R.id.recyclerProductos)
        recyclerProductos.layoutManager = LinearLayoutManager(this)

        cargarProductos()
    }

    private fun cargarProductos() {
        val apiService = RetrofitClient.getInstance(this)

        apiService.getProductos().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    val productos = response.body()!!
                    recyclerProductos.adapter = ProductoAdapter(productos)
                } else {
                    Toast.makeText(this@MostrarProductoActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@MostrarProductoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

