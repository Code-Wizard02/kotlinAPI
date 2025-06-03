package com.example.tiendita.auth

import com.example.tiendita.usuarios.Usuario

data class LoginResponse(
    val token: String?,
    val user: Usuario
)
