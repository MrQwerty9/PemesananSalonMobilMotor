package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TimeSlot(
    var id: Int = 0,
    var timeStart: String = "",
    var timeFinish: String = "",
    var available: Boolean = true
) : Parcelable