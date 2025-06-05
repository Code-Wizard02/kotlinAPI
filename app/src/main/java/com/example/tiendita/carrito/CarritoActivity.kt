package com.example.tiendita.carrito

import CarritoAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendita.R
import com.example.tiendita.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CarritoActivity : ComponentActivity() {

    private lateinit var carrito: ArrayList<CarritoItem>  // Ahora es accesible desde toda la clase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        carrito = intent.getSerializableExtra("carrito") as? ArrayList<CarritoItem> ?: arrayListOf()

        val recyclerCarrito = findViewById<RecyclerView>(R.id.recyclerCarrito)
        recyclerCarrito.layoutManager = LinearLayoutManager(this)


        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)
        fun calcularTotal(): Int {
            return carrito.sumOf { it.producto.precio * it.cantidad }
        }

        fun actualizarTotal() {
            val total = calcularTotal()
            txtTotal.text = "Total: $$total"
        }

        val adapter = CarritoAdapter(this, carrito) { _, _ ->
            actualizarTotal()
        }

        recyclerCarrito.adapter = adapter

        actualizarTotal()

        recyclerCarrito.adapter = adapter

        btnContinuar.setOnClickListener {
            if (carrito.isEmpty()) {
                Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ShippingFormActivity::class.java)
                intent.putExtra("carrito", carrito)
                startActivity(intent)
            }
        }


    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("carritoActualizado", carrito)
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }
}
