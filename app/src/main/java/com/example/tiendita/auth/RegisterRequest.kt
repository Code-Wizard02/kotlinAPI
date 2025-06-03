package com.example.tiendita.auth

data class RegisterRequest(
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val rol: String  // "0" para cliente, "1" para vendedor
)
