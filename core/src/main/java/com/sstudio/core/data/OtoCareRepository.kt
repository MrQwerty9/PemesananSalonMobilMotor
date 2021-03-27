package com.sstudio.core.data

import android.util.Log
import com.sstudio.core.data.source.remote.RemoteDataSource
import com.sstudio.core.data.source.remote.network.ApiResponse
import com.sstudio.core.domain.model.*
import com.sstudio.core.domain.repository.IOtoCareRepository
import com.sstudio.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

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

    override fun getBranchOfCity(city: String): Flow<Resource<List<Garage>>> =
        flow<Resource<List<Garage>>> {
            emit(Resource.Loading())
            when (val response = remoteDataSource.getBranchOfCity(city).first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(response.data.map {
                        DataMapper.mapGarageResponseToDomain(it)
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

    override fun getGarage(id: String): Flow<Resource<Garage>> {
        return flow { }
    }

    override fun setBooking(booking: Booking) {

    }
}