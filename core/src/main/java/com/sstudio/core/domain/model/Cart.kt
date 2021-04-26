package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val product: Product = Product(),
    var qty: Int = 0
) : Parcelable