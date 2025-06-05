package com.example.tiendita.carrito

import com.example.tiendita.produto.Product

data class CartItemResponse(
    val producto: Product,
    val cantidad: Int
)