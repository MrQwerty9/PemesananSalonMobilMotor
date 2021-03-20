package com.sstudio.otocare.listener

interface ILookbookLoadListener {
    fun onLookbookLoadSuccess(banner: List<com.sstudio.core.domain.model.Banner>)
    fun onLookbookLoadFailed(message: String)
}