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
        val btnPagar = findViewById<Button>(R.id.btnPagar)

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

        btnPagar.setOnClickListener {
            val api = RetrofitClient.getInstance(this)

            api.crearOrdenPayPal().enqueue(object : Callback<PayPalResponse> {
                override fun onResponse(call: Call<PayPalResponse>, response: Response<PayPalResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val url = response.body()!!.approveUrl
                        val browserIntent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
                        startActivity(browserIntent)
                    } else {
                        Toast.makeText(this@CarritoActivity, "Error al crear orden", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<PayPalResponse>, t: Throwable) {
                    Toast.makeText(this@CarritoActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
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
