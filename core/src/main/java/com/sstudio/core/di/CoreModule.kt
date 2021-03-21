package com.sstudio.core.di

import com.google.firebase.firestore.FirebaseFirestore
import com.sstudio.core.data.OtoCareRepository
import com.sstudio.core.data.source.remote.RemoteDataSource
import com.sstudio.core.domain.repository.IOtoCareRepository
import org.koin.dsl.module

val networkModule = module {
    single {
        FirebaseFirestore.getInstance()
    }
}
val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single<IOtoCareRepository> { OtoCareRepository(get()) }
}