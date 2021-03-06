package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Garage(
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var phone: String = "",
    var time: String = ""
) : Parcelable