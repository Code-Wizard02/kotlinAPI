package com.example.tiendita.data

import com.example.tiendita.produto.DeleteResponse
import com.example.tiendita.auth.LoginRequest
import com.example.tiendita.auth.LoginResponse
import com.example.tiendita.auth.RegisterRequest
import com.example.tiendita.carrito.AddToCartRequest
import com.example.tiendita.carrito.CartResponse
import com.example.tiendita.carrito.MessageResponse
import retrofit2.Call
import retrofit2.http.GET
import com.example.tiendita.produto.Product
import com.example.tiendita.produto.ProductRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PATCH

interface ApiService {

    @POST("api/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("api/products")
    fun getProductos(): Call<List<Product>>

    @POST("api/products")
    fun crearProducto(@Body product: ProductRequest): Call<Product>

    @GET("api/products/{id}")
    fun getProductoPorId(@Path("id") id: String): Call<Product>

    @PATCH("api/products/{id}")
    fun actualizarProducto(@Path("id") id: String, @Body producto: ProductRequest): Call<Product>

    @DELETE("api" +
            "/products/{id}")
    fun eliminarProducto(@Path("id") id: String): Call<DeleteResponse>

    @POST("api/auth/register")
    fun registrarUsuario(@Body usuario: RegisterRequest): Call<Void>

    @GET("api/cart")
    fun obtenerCarrito(): Call<CartResponse>

    @POST("api/cart/add")
    fun agregarProductoAlCarrito(@Body request: AddToCartRequest): Call<MessageResponse>

    @DELETE("api/cart/{productId}")
    fun eliminarProductoDelCarrito(@Path("productId") productId: String): Call<MessageResponse>
}
