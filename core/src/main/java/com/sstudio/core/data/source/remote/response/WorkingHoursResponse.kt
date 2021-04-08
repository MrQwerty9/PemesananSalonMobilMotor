package com.sstudio.core.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class WorkingHoursResponse(
    var id: Int = 0,
    var start: String = "",
    var finish: String = ""
) : Parcelable