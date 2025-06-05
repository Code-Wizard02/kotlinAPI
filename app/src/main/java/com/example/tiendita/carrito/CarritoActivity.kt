package com.example.tiendita.carrito

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendita.R

class CarritoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val carrito = intent.getSerializableExtra("carrito") as? ArrayList<CarritoItem> ?: arrayListOf()

        val recyclerCarrito = findViewById<RecyclerView>(R.id.recyclerCarrito)
        recyclerCarrito.layoutManager = LinearLayoutManager(this)
        val adapter = CarritoAdapter(carrito) { pos, nuevaCantidad ->
            // Aquí puedes guardar cambios, actualizar UI, etc.
            Toast.makeText(this, "Cantidad actualizada: $nuevaCantidad", Toast.LENGTH_SHORT).show()
        }
        recyclerCarrito.adapter = adapter
    }
}
