package com.example.tiendita.carrito

data class AddToCartRequest(
    val productId: String,
    val cantidad: Int
)