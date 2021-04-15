package com.sstudio.core.domain.model

data class Product(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val category: Int = 0
)