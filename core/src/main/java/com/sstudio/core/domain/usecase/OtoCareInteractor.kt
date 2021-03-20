package com.sstudio.core.domain.usecase

import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.*
import com.sstudio.core.domain.repository.IOtoCareRepository
import kotlinx.coroutines.flow.Flow

class OtoCareInteractor(private val iOtoCareRepository: IOtoCareRepository) : OtoCareUseCase {
    override fun getUser(userPhone: String): Flow<Resource<User>> =
        iOtoCareRepository.getUser(userPhone)

    override fun setUser(user: User): Flow<Resource<String>> =
        iOtoCareRepository.setUser(user)

    override fun getHomeBanner(): Flow<Resource<List<Banner>>> =
        iOtoCareRepository.getHomeBanner()

    override fun getHomeLookBook(): Flow<Resource<List<Banner>>> =
        iOtoCareRepository.getHomeLookBook()

    override fun getAllGarage(): Flow<Resource<List<Garage>>> =
        iOtoCareRepository.getAllGarage()

    override fun getGarage(id: String): Flow<Resource<Garage>> =
        iOtoCareRepository.getGarage(id)

    override fun getAllSalon(): Flow<Resource<List<Salon>>> =
        iOtoCareRepository.getAllSalon()

    override fun getSalon(id: String): Flow<Resource<Salon>> =
        iOtoCareRepository.getSalon(id)

    override fun setBooking(booking: Booking) =
        iOtoCareRepository.setBooking(booking)
}