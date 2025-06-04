package com.example.tiendita.carrito

import com.example.tiendita.produto.Product
import java.io.Serializable

data class CarritoItem(
    val id: String,
    val producto: Product,
    var cantidad: Int = 0
) : Serializable

