package com.example.tiendita.usuarios

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("_id") val id: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val rol: Int
)
