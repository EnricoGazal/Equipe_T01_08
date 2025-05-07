package com.example.geowarning.network
import com.example.geowarning.User.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @POST("users")
    fun createUser(@Body user: User): Call<Void>

    @GET("getUser")
    fun getUser(@Query("email") email: String, @Query("password") password: String): Call<User?>
}