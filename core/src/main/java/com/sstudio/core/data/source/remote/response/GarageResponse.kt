package com.sstudio.core.data.source.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GarageResponse(
    var name: String,
    var username: String,
    var password: String,
    var rating: Int,
    var id: String
) : Parcelable {
    constructor() : this("", "", "", 0, "")
}