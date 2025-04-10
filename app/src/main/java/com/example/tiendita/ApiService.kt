package com.example.tiendita

import retrofit2.Call
import retrofit2.http.GET
import com.example.tiendita.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PATCH
import retrofit2.http.Query

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

    @DELETE("api/products/{id}")
    fun eliminarProducto(@Path("id") id: String): Call<DeleteResponse>




}
