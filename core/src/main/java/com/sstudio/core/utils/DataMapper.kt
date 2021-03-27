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

    fun mapTimeSlotResponseToDomain(input: TimeSlotResponse): TimeSlot =
        TimeSlot(
            slot = input.slot
        )

    fun mapCityResponseToDomain(input: CityResponse): City =
        City(
            id = input.id,
            name = input.name
        )
}