package com.sstudio.otocare.listener

interface IBannerLoadListener {
    fun onBannerLoadSuccess(banner: List<com.sstudio.core.domain.model.Banner>)
    fun onBannerLoadFailed(message: String)
}