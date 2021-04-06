package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class WorkingHour(
    var id: Int = 0,
    var time: String = ""
) : Parcelable