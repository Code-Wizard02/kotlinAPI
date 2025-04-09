package com.example.tiendita

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("_id") val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int
)

