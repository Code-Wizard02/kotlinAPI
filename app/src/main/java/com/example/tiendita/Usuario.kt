package com.example.tiendita

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("_id") val id: String,
    val nombre: String,
    val email: String,
)


