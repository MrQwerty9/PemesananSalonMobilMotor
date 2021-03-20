package com.sstudio.core.domain.repository

import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface IOtoCareRepository {
    fun getUser(userPhone: String): Flow<Resource<User>>
    fun setUser(user: User): Flow<Resource<String>>

    fun getHomeBanner(): Flow<Resource<List<Banner>>>
    fun getHomeLookBook(): Flow<Resource<List<Banner>>>

    fun getAllGarage(): Flow<Resource<List<Garage>>>
    fun getGarage(id: String): Flow<Resource<Garage>>

    fun getAllSalon(): Flow<Resource<List<Salon>>>
    fun getSalon(id: String): Flow<Resource<Salon>>

    fun setBooking(booking: Booking)
}