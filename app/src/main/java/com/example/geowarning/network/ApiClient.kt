package com.example.geowarning.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit: ApiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/api/") // ← importante usar isso para acessar o localhost do host no emulador
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}