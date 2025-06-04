package com.example.tiendita.vendedor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.tiendita.R
import com.example.tiendita.produto.ActualizarListaProductosActivity
import com.example.tiendita.produto.CrearProductoActivity
import com.example.tiendita.produto.EliminarProductoActivity
import com.example.tiendita.produto.MostrarProductoActivity

class DashboardVendedorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnCrear: Button = findViewById(R.id.btnCrear)
        val btnVerProductos: Button = findViewById(R.id.btnVerProductos)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminarProducto = findViewById<Button>(R.id.btnEliminarProducto)

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

        btnEliminarProducto.setOnClickListener {
            val intent = Intent(this, EliminarProductoActivity::class.java)
            startActivity(intent)
        }
    }
}
