package com.sstudio.core.data.source.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SalonResponse(
    val name: String,
    val address: String,
    var id: String,
    var phone: String,
    var time: String
) : Parcelable {
    constructor() : this("", "", "", "", "")
}