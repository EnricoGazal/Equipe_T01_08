package com.example.geowarning.User

data class User(
    val email: String,
    val password: String,
    val position: String? = null
)
