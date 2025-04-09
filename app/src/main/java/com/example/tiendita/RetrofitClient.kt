package com.example.tiendita

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.74:3000/"

    fun getInstance(context: Context): ApiService {
        val sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""  // Obtener el token de SharedPreferences

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")  // Agregar el token a las cabeceras
                .build()
            chain.proceed(request)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())  // Usar el convertidor Gson
            .build()

        return retrofit.create(ApiService::class.java)  // Crear la instancia de ApiService
    }
}


