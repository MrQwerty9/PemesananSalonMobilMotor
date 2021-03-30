package com.sstudio.core.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class WorkingHoursResponse(
    var id: Int = 0,
    var time: String = ""
) : Parcelable