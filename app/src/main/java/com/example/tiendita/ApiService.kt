package com.example.tiendita

import retrofit2.Call
import retrofit2.http.GET
import com.example.tiendita.Product

interface ApiService {
    @GET("api/products")
    fun getProducts(): Call<List<Product>>
}
