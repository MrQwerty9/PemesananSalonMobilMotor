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

    override fun getAllPackage(): Flow<Resource<List<Package>>> =
        iOtoCareRepository.getAllPackage()

    override fun getTeam(id: String): Flow<Resource<Package>> =
        iOtoCareRepository.getTeam(id)

    override fun getAllCityOfGarage(): Flow<Resource<List<City>>> =
        iOtoCareRepository.getAllCityOfGarage()

    override fun getBranchOfCity(city: String): Flow<Resource<List<Garage>>> =
        iOtoCareRepository.getBranchOfCity(city)

    override fun getTimeSlot(date: String, garageId: String): Flow<Resource<List<TimeSlot>>> =
        iOtoCareRepository.getTimeSlot(date, garageId)

    override fun setBooking(booking: Booking): Flow<Resource<String>> =
        iOtoCareRepository.setBooking(booking)

    override fun setCancelBooking(activeBookingId: String): Flow<Resource<String>> =
        iOtoCareRepository.setCancelBooking(activeBookingId)

    override fun getBookingInformation(userPhone: String): Flow<Resource<Booking?>> =
        iOtoCareRepository.getBookingInformation(userPhone)

    override fun getProduct(category: Int): Flow<Resource<List<Product>>> =
        iOtoCareRepository.getProduct(category)

    override fun getCategoryProduct(): Flow<Resource<List<CategoryProduct>>> =
        iOtoCareRepository.getCategoryProduct()

    override fun getCart(userPhone: String): Flow<Resource<Cart>> =
        iOtoCareRepository.getCart(userPhone)

    override fun setCart(userPhone: String, productId: String): Flow<Resource<String>> =
        iOtoCareRepository.setCart(userPhone, productId)
}