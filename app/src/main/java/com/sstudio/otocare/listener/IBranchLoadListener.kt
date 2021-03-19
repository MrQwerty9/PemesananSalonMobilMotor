package com.sstudio.otocare.listener

import com.sstudio.otocare.Model.Salon

interface IBranchLoadListener {
    fun onBranchLoadSuccess(salonList: List<Salon>)
    fun onBranchLoadFailed(message: String)
}