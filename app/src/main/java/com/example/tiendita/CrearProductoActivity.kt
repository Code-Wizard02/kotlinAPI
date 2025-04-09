package com.example.tiendita

import android.os.Bundle
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.activity.ComponentActivity

class CrearProductoActivity : ComponentActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var btnCrearProducto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_producto)

        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        etPrecio = findViewById(R.id.etPrecio)
        etStock = findViewById(R.id.etStock)
        btnCrearProducto = findViewById(R.id.btnCrearProducto)

        btnCrearProducto.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val precio = etPrecio.text.toString().toIntOrNull()
            val stock = etStock.text.toString().toIntOrNull()

            if (nombre.isEmpty() || descripcion.isEmpty() || precio == null || stock == null) {
                Toast.makeText(this, "Por favor completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoProducto = ProductRequest(nombre, descripcion, precio, stock)

            // Usar RetrofitClient para hacer la solicitud de creación del producto
            val apiService = RetrofitClient.getInstance(this)
            apiService.crearProducto(nuevoProducto)
                .enqueue(object : Callback<Product> {
                    override fun onResponse(call: Call<Product>, response: Response<Product>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@CrearProductoActivity, "Producto creado exitosamente", Toast.LENGTH_SHORT).show()
                            finish()  // Finalizar la actividad y regresar
                        } else {
                            Toast.makeText(this@CrearProductoActivity, "Error al crear producto", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Product>, t: Throwable) {
                        Toast.makeText(this@CrearProductoActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
