package com.example.tiendita.produto

data class ProductRequest(
    val nombre: String,
    val descripcion: String,
    val precio:Int,
    val stock: Int
)

