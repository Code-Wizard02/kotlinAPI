package com.example.tiendita.produto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.tiendita.R
import com.example.tiendita.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioActualizarProductoActivity : ComponentActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var btnActualizar: Button
    private lateinit var producto: Product
    private lateinit var idProducto: String // Para almacenar el ID del producto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_producto)

        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        etPrecio = findViewById(R.id.etPrecio)
        etStock = findViewById(R.id.etStock)
        btnActualizar = findViewById(R.id.btnActualizarProducto)

        // Recuperar el producto seleccionado de la Intent
        producto = intent.getSerializableExtra("producto") as Product

        // Asignar los valores del producto a los EditText
        etNombre.setText(producto.nombre)
        etDescripcion.setText(producto.descripcion)
        etPrecio.setText(producto.precio.toString())
        etStock.setText(producto.stock.toString())

        // Obtener el ID del producto
        idProducto = producto.id

        btnActualizar.setOnClickListener {
            // Crear el objeto ProductRequest con los datos actualizados
            val updatedProduct = ProductRequest(
                nombre = etNombre.text.toString().trim(),
                descripcion = etDescripcion.text.toString().trim(),
                precio = etPrecio.text.toString().toIntOrNull() ?: 0,
                stock = etStock.text.toString().toIntOrNull() ?: 0
            )

            // Enviar la solicitud PATCH a la API
            RetrofitClient.getInstance(this).actualizarProducto(idProducto, updatedProduct)
                .enqueue(object : Callback<Product> {
                    override fun onResponse(call: Call<Product>, response: Response<Product>) {
                        if (response.isSuccessful) {
                            // Actualización exitosa
                            Toast.makeText(this@FormularioActualizarProductoActivity, "Producto actualizado", Toast.LENGTH_SHORT).show()

                            // Pasar el ID del producto actualizado a la actividad de lista
                            val intent = Intent()
                            intent.putExtra("idProductoActualizado", idProducto)
                            setResult(RESULT_OK, intent)  // Establecer el resultado para la actividad que lo llamó
                            finish()  // Terminar la actividad después de actualizar
                        } else {
                            // Error al actualizar
                            Toast.makeText(this@FormularioActualizarProductoActivity, "Error al actualizar producto", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Product>, t: Throwable) {
                        Toast.makeText(this@FormularioActualizarProductoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
