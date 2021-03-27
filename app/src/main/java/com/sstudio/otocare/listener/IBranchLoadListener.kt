package com.sstudio.otocare.listener

interface IBranchLoadListener {
    fun onBranchLoadSuccess(garageList: List<com.sstudio.core.domain.model.Garage>)
    fun onBranchLoadFailed(message: String)
}