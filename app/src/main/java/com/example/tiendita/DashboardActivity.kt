package com.example.tiendita

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnCrear: Button = findViewById(R.id.btnCrear)
        val btnVerProductos: Button = findViewById(R.id.btnVerProductos)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        btnCrear.setOnClickListener {
            startActivity(Intent(this, CrearProductoActivity::class.java))
        }

        btnVerProductos.setOnClickListener {
            val intent = Intent(this, MostrarProductoActivity::class.java)
            startActivity(intent)
        }

        btnActualizar.setOnClickListener {
            val intent = Intent(this, ActualizarListaProductosActivity::class.java)
            startActivity(intent)
        }
    }
}

