package com.example.tiendita.produto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @SerializedName("_id") val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int
) : Serializable
