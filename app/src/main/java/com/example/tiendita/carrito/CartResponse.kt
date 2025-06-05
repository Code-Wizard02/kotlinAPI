package com.example.tiendita.carrito

data class CartResponse(
    val _id: String,
    val comprador: String,
    val productos: List<CartItemResponse>
)