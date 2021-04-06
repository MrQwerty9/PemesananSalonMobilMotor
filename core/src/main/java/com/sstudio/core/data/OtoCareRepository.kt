package com.sstudio.core.data

import android.util.Log
import com.sstudio.core.data.source.remote.RemoteDataSource
import com.sstudio.core.data.source.remote.network.ApiResponse
import com.sstudio.core.domain.model.*
import com.sstudio.core.domain.repository.IOtoCareRepository
import com.sstudio.core.utils.DataMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OtoCareRepository(
    private val remoteDataSource: RemoteDataSource
) : IOtoCareRepository {
    override fun getUser(userPhone: String): Flow<Resource<User>> =
        flow<Resource<User>> {
            emit(Resource.Loading())
            when (val userResponse = remoteDataSource.getUser(userPhone).first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(DataMapper.mapUserResponseToDomain(userResponse.data)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(User()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(userResponse.errorMessage))
                }
            }
        }

    override fun setUser(user: User): Flow<Resource<String>> =
        flow<Resource<String>> {
            emit(Resource.Loading())
            when (val userResponse = remoteDataSource.setUser(user).first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(""))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(userResponse.errorMessage))
                }
            }
        }

    override fun getHomeBanner(): Flow<Resource<List<Banner>>> {
        return flow<Resource<List<Banner>>> {
            emit(Resource.Loading())
            when (val userResponse = remoteDataSource.getHomeBanner().first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(userResponse.data.map {
                        DataMapper.mapBannerResponseToDomain(it)
                    }))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(userResponse.errorMessage))
                }
            }

        }
    }

    override fun getHomeLookBook(): Flow<Resource<List<Banner>>> =
        flow<Resource<List<Banner>>> {
            emit(Resource.Loading())
            when (val response = remoteDataSource.getHomeLookBook().first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(response.data.map {
                        DataMapper.mapBannerResponseToDomain(it)
                    }))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun getAllPackage(): Flow<Resource<List<Package>>> {
        return flow<Resource<List<Package>>> {
            emit(Resource.Loading())
            when (val response = remoteDataSource.getAllPackage().first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(response.data.map {
                        Log.d("mytag", "repo ${it}")
                        DataMapper.mapPackageResponseToDomain(it)
                    }))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }
    }

    override fun getTeam(id: String): Flow<Resource<Package>> {
        return flow { }
    }

    override fun getAllCityOfGarage(): Flow<Resource<List<City>>> =
        flow<Resource<List<City>>> {
            emit(Resource.Loading())
            when (val response = remoteDataSource.getAllCityOfGarage().first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(response.data.map {
                        DataMapper.mapCityResponseToDomain(it)
                    }))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    @ExperimentalCoroutinesApi
    override fun getBranchOfCity(city: String): Flow<Resource<List<Garage>>> =
        flow<Resource<List<Garage>>> {
            emit(Resource.Loading())
            emitAll(remoteDataSource.getBranchOfCity(city).map { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        Resource.Success(response.data.map {
                            DataMapper.mapGarageResponseToDomain(it)
                        })
                    }
                    is ApiResponse.Empty -> {
                        Resource.Success(listOf())
                    }
                    is ApiResponse.Error -> {
                        Resource.Error(response.errorMessage)
                    }
                }
            }
            )
        }

    @ExperimentalCoroutinesApi
    override fun getTimeSlot(date: String, garageId: String): Flow<Resource<List<TimeSlot>>> {
        return flow<Resource<List<TimeSlot>>> {
            emit(Resource.Loading())
            when (val response = remoteDataSource.getWorkingHours().first()) {
                is ApiResponse.Success -> {
                    val data = response.data.map {
                        DataMapper.mapWorkingHoursResponseToDomain(it)
                    }
                    emitAll(
                        remoteDataSource.getTimeSlotBooked(date, garageId).map { responseTimeSlot ->
                            when (responseTimeSlot) {
                                is ApiResponse.Success -> {
                                    val bookedTimeSlot = responseTimeSlot.data
                                    for (i in data) {
                                        i.available = !bookedTimeSlot.contains(i.id.toString())
                                    }
                                    Resource.Success(data)
                                }
                                is ApiResponse.Empty ->
                                    Resource.Success(listOf())
                                is ApiResponse.Error ->
                                    Resource.Error(responseTimeSlot.errorMessage)
                            }
                        })
                }
                is ApiResponse.Empty -> {
                    Resource.Success<List<TimeSlot>>(listOf())
                }
                is ApiResponse.Error -> {
                    Resource.Error<List<TimeSlot>>(response.errorMessage)
                }
            }
        }
    }

    override fun setBooking(booking: Booking): Flow<Resource<String>> =
        flow<Resource<String>> {
            emit(Resource.Loading())
            when (val userResponse = remoteDataSource.setBooking(
                DataMapper.mapBookingToBookingResponse(booking)
            ).first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(""))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(userResponse.errorMessage))
                }
            }
        }

    override fun getBookingInformation(userPhone: String): Flow<Resource<Booking>> =
        flow<Resource<Booking>> {
            emit(Resource.Loading())
            var booking = Booking()
            when (val bookingResponse = remoteDataSource.getBookingUser(userPhone).first()) {
                is ApiResponse.Success -> {
                    for (response in bookingResponse.data) {
                        Log.d("mytag", "booking info $response")
                        if (strToDate(response.date)?.after(Calendar.getInstance().time) == true ||
                            strToDate(response.date)?.equals(Calendar.getInstance().time) == true
                        ) {

                            booking = DataMapper.mapBookingResponseToDomain(response)
                            booking.customer = getUser(response.userPhone).first().data ?: User()
                        }
                    }
                    emit(Resource.Success(booking))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(bookingResponse.errorMessage))
                }
            }
        }

    private fun strToDate(date: String): Date? {
        val df: DateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
        return try {
            df.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}