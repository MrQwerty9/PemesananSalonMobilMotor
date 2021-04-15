package com.sstudio.otocare.di

import com.sstudio.core.domain.usecase.OtoCareInteractor
import com.sstudio.core.domain.usecase.OtoCareUseCase
import com.sstudio.otocare.ui.booking.BookingViewModel
import com.sstudio.otocare.ui.home.HomeViewModel
import com.sstudio.otocare.ui.shop.ShopViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<OtoCareUseCase> { OtoCareInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { BookingViewModel(get()) }
    viewModel { ShopViewModel(get()) }
}