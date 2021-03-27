package com.sstudio.core.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GarageResponse(
    var id: String = "",
    val name: String = "",
    val address: String = "",
    var phone: String = "",
    var time: String = ""
) : Parcelable