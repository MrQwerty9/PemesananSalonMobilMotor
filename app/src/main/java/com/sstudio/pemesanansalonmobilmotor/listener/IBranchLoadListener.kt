package com.sstudio.pemesanansalonmobilmotor.listener

import com.sstudio.pemesanansalonmobilmotor.Model.Banner
import com.sstudio.pemesanansalonmobilmotor.Model.Salon

interface IBranchLoadListener {
    fun onBranchLoadSuccess(salonList: List<Salon>)
    fun onBranchLoadFailed(message: String)
}