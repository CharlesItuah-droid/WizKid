package com.example.wizkid.model

data class Subject(
    val id: String, // Or Int, depending on your needs
    val name: String,
    val description: String? = null,
    // Add other relevant fields like icon, color, etc.
)