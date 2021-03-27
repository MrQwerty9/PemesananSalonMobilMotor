package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String = "name",
    val address: String = "address",
    var phoneNumber: String = "phone_number"
) : Parcelable
