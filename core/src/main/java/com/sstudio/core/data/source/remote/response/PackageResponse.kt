package com.sstudio.core.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PackageResponse(
    var id: String = "",
    var name: String = "",
    var include: List<String> = listOf(),
    var price: Double = 0.0,
    var popular: Boolean = false,
    var image: String = ""
) : Parcelable