package com.example.tiendita.carrito

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendita.R
import com.example.tiendita.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast

class ShippingFormActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etCiudad: EditText
    private lateinit var etCP: EditText
    private lateinit var etPais: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping_form)

        etNombre = findViewById(R.id.etNombre)
        etDireccion = findViewById(R.id.etDireccion)
        etCiudad = findViewById(R.id.etCiudad)
        etCP = findViewById(R.id.etCP)
        etPais = findViewById(R.id.etPais)

        val btnPagar = findViewById<Button>(R.id.btnPagar)

        btnPagar.setOnClickListener {
            val api = RetrofitClient.getInstance(this)

            api.crearOrdenPayPal().enqueue(object : Callback<PayPalResponse> {
                override fun onResponse(call: Call<PayPalResponse>, response: Response<PayPalResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val url = response.body()!!.approveUrl
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(browserIntent)
                    } else {
                        Toast.makeText(this@ShippingFormActivity, "Error al crear orden", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<PayPalResponse>, t: Throwable) {
                    Toast.makeText(this@ShippingFormActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

    }
}