package com.sstudio.otocare.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bengkel(var nama: String, var username: String, var password: String, var rating: Int, var bengkelId: String): Parcelable{
    constructor() : this("","","",0,"")
}