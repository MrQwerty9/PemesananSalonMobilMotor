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

    fun mapGarageResponseToDomain(input: GarageResponse): Garage =
        Garage(
            name = input.name,
            id = input.id,
            username = input.username,
            password = input.password,
            rating = input.rating
        )

    fun mapSalonResponseToDomain(input: SalonResponse): Salon =
        Salon(
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
}