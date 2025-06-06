package com.example.tiendita.carrito

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendita.R
import com.example.tiendita.cliente.DashboardClienteActivity

class ConfirmacionCompraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacion_compra)

        val btnVolverInicio = findViewById<Button>(R.id.btnVolverInicio)
        btnVolverInicio.setOnClickListener {
            // Volver a la pantalla principal y limpiar el historial de actividades
            val intent = Intent(this, DashboardClienteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}