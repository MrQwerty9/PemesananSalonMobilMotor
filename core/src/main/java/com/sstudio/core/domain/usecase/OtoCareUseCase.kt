package com.sstudio.core.domain.usecase

import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface OtoCareUseCase {
    fun getUser(userPhone: String): Flow<Resource<User>>
    fun setUser(user: User): Flow<Resource<String>>

    fun getHomeBanner(): Flow<Resource<List<Banner>>>
    fun getHomeLookBook(): Flow<Resource<List<Banner>>>

    fun getAllPackage(): Flow<Resource<List<Package>>>
    fun getTeam(id: String): Flow<Resource<Package>>

    fun getAllCityOfGarage(): Flow<Resource<List<City>>>
    fun getBranchOfCity(city: String): Flow<Resource<List<Garage>>>

    fun getTimeSlot(date: String, garageId: String): Flow<Resource<List<TimeSlot>>>

    fun setBooking(booking: Booking): Flow<Resource<String>>
    fun setCancelBooking(activeBookingId: String): Flow<Resource<String>>
    fun getBookingInformation(userPhone: String): Flow<Resource<Booking?>>

    fun getProduct(category: Int): Flow<Resource<List<Product>>>
    fun getCategoryProduct(): Flow<Resource<List<CategoryProduct>>>

    fun getCart(userPhone: String): Flow<Resource<List<Cart>>>
    fun setCart(userPhone: String, cart: Cart): Flow<Resource<String>>
    fun addCart(userPhone: String, productId: String): Flow<Resource<String>>
    fun deleteCart(userPhone: String, cart: Cart): Flow<Resource<String>>
}