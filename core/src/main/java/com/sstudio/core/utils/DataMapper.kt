package com.sstudio.core.utils

import com.sstudio.core.data.source.remote.response.*
import com.sstudio.core.domain.model.*

object DataMapper {
    fun mapUserResponseToDomain(input: UserResponse): User =
        User(
            name = input.name,
            address = input.address,
            phoneNumber = input.phoneNumber
        )

    fun mapBannerResponseToDomain(input: BannerResponse): Banner =
        Banner(
            image = input.image
        )

    fun mapPackageResponseToDomain(input: PackageResponse): Package =
        Package(
            id = input.id,
            name = input.name,
            include = input.include,
            price = input.price,
            popular = input.popular,
            image = input.image
        )

    fun mapGarageResponseToDomain(input: GarageResponse): Garage =
        Garage(
            name = input.name,
            address = input.address,
            phone = input.phone,
            time = input.time,
            id = input.id
        )

    fun mapWorkingHoursResponseToDomain(input: WorkingHoursResponse): TimeSlot =
        TimeSlot(
            timeSlot = input.time,
            id = input.id,
            available = false
        )

    fun mapTimeSlotResponseToDomain(input: TimeSlotResponse): TimeSlot =
        TimeSlot(
            timeSlot = input.slot.toString()
        )

    fun mapCityResponseToDomain(input: CityResponse): City =
        City(
            id = input.id,
            name = input.name
        )

    fun mapBookingToBookingResponse(input: Booking): BookingResponse =
        BookingResponse(
            customerId = input.customer.phoneNumber,
            date = input.date,
            garageId = input.garage.id,
            packageId = input.pkg.id,
            timeSlotId = input.timeSlot.id.toString()
        )
}