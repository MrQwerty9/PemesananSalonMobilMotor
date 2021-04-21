package com.sstudio.core.domain.model

data class Cart(
    val product: Product = Product(),
    var qty: Int = 0
)