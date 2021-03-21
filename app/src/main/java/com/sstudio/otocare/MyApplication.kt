package com.sstudio.otocare

import android.app.Application
import com.sstudio.core.di.networkModule
import com.sstudio.core.di.repositoryModule
import com.sstudio.otocare.di.useCaseModule
import com.sstudio.otocare.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    useCaseModule,
                    viewModelModule,
                    networkModule,
                    repositoryModule
                )
            )
        }
    }
}