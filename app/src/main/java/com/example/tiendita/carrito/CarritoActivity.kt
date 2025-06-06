package com.example.tiendita.carrito

                import CarritoAdapter
                import android.content.Intent
                import android.os.Bundle
                import android.view.View
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
                    private lateinit var txtTotal: TextView
                    private lateinit var btnContinuar: Button
                    private lateinit var recyclerCarrito: RecyclerView
                    private lateinit var adapter: CarritoAdapter

                    companion object {
                        private const val REQUEST_CODE_SHIPPING = 100
                    }

                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        setContentView(R.layout.activity_carrito)

                        carrito = intent.getSerializableExtra("carrito") as? ArrayList<CarritoItem> ?: arrayListOf()

                        recyclerCarrito = findViewById(R.id.recyclerCarrito)
                        recyclerCarrito.layoutManager = LinearLayoutManager(this)

                        txtTotal = findViewById(R.id.txtTotal)
                        btnContinuar = findViewById(R.id.btnContinuar)

                        fun calcularTotal(): Int {
                            return carrito.sumOf { it.producto.precio * it.cantidad }
                        }

                        fun actualizarTotal() {
                            val total = calcularTotal()
                            txtTotal.text = "Total: $$total"
                        }

                        adapter = CarritoAdapter(this, carrito) { _, _ ->
                            actualizarTotal()
                        }

                        recyclerCarrito.adapter = adapter

                        actualizarTotal()

                        btnContinuar.setOnClickListener {
                            if (carrito.isEmpty()) {
                                Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show()
                            } else {
                                val intent = Intent(this, ShippingFormActivity::class.java)
                                intent.putExtra("carrito", carrito)
                                // Usar startActivityForResult en lugar de startActivity
                                startActivityForResult(intent, REQUEST_CODE_SHIPPING)
                            }
                        }

                        // Verificar si el carrito está vacío inicialmente
                        if (carrito.isEmpty()) {
                            actualizarUICarritoVacio()
                        }
                    }

                    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                        super.onActivityResult(requestCode, resultCode, data)
                        if (requestCode == REQUEST_CODE_SHIPPING && resultCode == RESULT_OK) {
                            if (data?.getBooleanExtra("compraRealizada", false) == true) {
                                // Vaciar el carrito
                                carrito.clear()
                                // Actualizar la interfaz para mostrar carrito vacío
                                adapter.notifyDataSetChanged()
                                actualizarUICarritoVacio()
                                // Mostrar mensaje de éxito
                                Toast.makeText(this, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    private fun actualizarUICarritoVacio() {
                        // Ocultar botón de continuar y total si el carrito está vacío
                        btnContinuar.visibility = View.GONE
                        txtTotal.visibility = View.GONE

                        // Mostrar mensaje de carrito vacío (asegúrate de tener este TextView en tu layout)
                        val tvCartEmpty = findViewById<TextView>(R.id.tvCartEmpty)
                        if (tvCartEmpty != null) {
                            tvCartEmpty.visibility = View.VISIBLE
                        } else {
                            // Si no existe el TextView, muestra un Toast
                            Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onBackPressed() {
                        val intent = Intent()
                        intent.putExtra("carritoActualizado", carrito)
                        setResult(RESULT_OK, intent)
                        super.onBackPressed()
                    }
                }