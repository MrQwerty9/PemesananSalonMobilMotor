package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Garage(
    var name: String = "",
    var username: String = "",
    var password: String = "",
    var rating: Int = 0,
    var id: String = ""
) : Parcelable