package com.sstudio.otocare.listener

interface IBranchLoadListener {
    fun onBranchLoadSuccess(salonList: List<com.sstudio.core.domain.model.Salon>)
    fun onBranchLoadFailed(message: String)
}