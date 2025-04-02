package com.example.tiendita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tiendita.ui.theme.TienditaTheme

import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.tiendita.RetrofitClient
import com.example.tiendita.ApiService


class MainActivity : ComponentActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)


        val call = RetrofitClient.instance.getProducts()

        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val productList = response.body()
                    if (productList != null) {
                        // Mostrar los productos en la UI
                        textView.text = productList.joinToString("\n") { "${it.nombre} - ${it.precio}" }
                        Log.d("API_RESPONSE", "Productos cargados: ${productList.size}")
                    } else {
                        Log.e("API_RESPONSE", "La lista de productos está vacía")
                    }
                } else {
                    Log.e("API_RESPONSE", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("API_RESPONSE", "Error de conexión: ${t.message}")
            }
        })
    }
}

