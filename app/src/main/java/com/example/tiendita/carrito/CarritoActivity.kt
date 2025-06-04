package com.example.tiendita.carrito

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.tiendita.R

class CarritoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val carrito = intent.getSerializableExtra("carrito") as? ArrayList<CarritoItem> ?: arrayListOf()

        // Por ahora no hacemos nada con el carrito, solo que no crashee.
    }
}
