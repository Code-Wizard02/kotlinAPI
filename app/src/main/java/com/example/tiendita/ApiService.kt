package com.example.tiendita

import retrofit2.Call
import retrofit2.http.GET
import com.example.tiendita.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("api/products")
    fun getProducts(): Call<List<Product>>

    @POST("api/auth/login")
    fun login(@Body credentials: LoginRequest): Call<LoginResponse>

}
