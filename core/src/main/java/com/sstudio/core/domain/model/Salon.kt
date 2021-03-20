package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Salon(
    val name: String = "",
    val address: String = "",
    var id: String = "",
    var phone: String = "",
    var time: String = ""
) : Parcelable