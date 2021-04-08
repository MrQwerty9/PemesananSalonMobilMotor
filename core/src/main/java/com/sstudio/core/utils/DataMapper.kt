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
            timeStart = input.start,
            timeFinish = input.finish,
            id = input.id,
            available = false
        )

    fun mapCityResponseToDomain(input: CityResponse): City =
        City(
            id = input.id,
            name = input.name
        )

    fun mapBookingToBookingResponse(input: Booking): BookingResponse =
        BookingResponse(
            userPhone = input.customer.phoneNumber,
            date = input.date,
            garageId = input.garage.id,
            garageName = input.garage.name,
            garageAddress = input.garage.address,
            garagePhone = input.garage.phone,
            packageId = input.pkg.id,
            packageName = input.pkg.name,
            packageInclude = input.pkg.include,
            packagePrice = input.pkg.price,
            timeStart = input.timeSlot.timeStart,
            timeFinish = input.timeSlot.timeFinish,
            timeId = input.timeSlot.id.toString()
        )

    fun mapBookingResponseToDomain(input: BookingResponse): Booking =
        Booking(
            id = input.id,
            timeSlot = TimeSlot(
                id = input.timeId.toInt(),
                timeStart = input.timeStart,
                timeFinish = input.timeFinish
            ),
            date = input.date,
            pkg = Package(
                id = input.packageId,
                name = input.packageName,
                include = input.packageInclude,
                price = input.packagePrice
            ),
            garage = Garage(
                id = input.packageId,
                name = input.garageName,
                address = input.garageAddress,
                phone = input.garagePhone
            ),
        )
}