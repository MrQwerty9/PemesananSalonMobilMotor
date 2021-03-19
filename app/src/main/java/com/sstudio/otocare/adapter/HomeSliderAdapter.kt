package com.sstudio.otocare.adapter

import com.sstudio.otocare.Model.Banner
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class HomeSliderAdapter(val bannerList: List<Banner>): SliderAdapter() {

    override fun getItemCount(): Int {
        return bannerList!!.size
    }

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        imageSlideViewHolder!!.bindImageSlide(bannerList!!.get(position).image)
    }
}