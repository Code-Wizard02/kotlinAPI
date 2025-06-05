package com.example.tiendita.carrito

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendita.R

class CarritoActivity : ComponentActivity() {

    private lateinit var carrito: ArrayList<CarritoItem>  // Ahora es accesible desde toda la clase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        carrito = intent.getSerializableExtra("carrito") as? ArrayList<CarritoItem> ?: arrayListOf()

        val recyclerCarrito = findViewById<RecyclerView>(R.id.recyclerCarrito)
        recyclerCarrito.layoutManager = LinearLayoutManager(this)


        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

        fun calcularTotal(): Int {
            return carrito.sumOf { it.producto.precio * it.cantidad }
        }

        fun actualizarTotal() {
            val total = calcularTotal()
            txtTotal.text = "Total: $$total"
        }

        val adapter = CarritoAdapter(carrito) { _, _ ->
            actualizarTotal()
        }
        recyclerCarrito.adapter = adapter

        actualizarTotal()

        btnPagar.setOnClickListener {
            Toast.makeText(this, "Implementar proceso de pago...", Toast.LENGTH_SHORT).show()
        }

        recyclerCarrito.adapter = adapter
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("carritoActualizado", carrito)
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }
}
