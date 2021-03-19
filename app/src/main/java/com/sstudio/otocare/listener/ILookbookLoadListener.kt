package com.sstudio.otocare.listener

import com.sstudio.otocare.Model.Banner

interface ILookbookLoadListener {
    fun onLookbookLoadSuccess(banner: List<Banner>)
    fun onLookbookLoadFailed(message: String)
}