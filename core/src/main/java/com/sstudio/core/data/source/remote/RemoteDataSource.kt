package com.sstudio.core.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.sstudio.core.data.Resource
import com.sstudio.core.data.source.remote.network.ApiResponse
import com.sstudio.core.data.source.remote.response.BannerResponse
import com.sstudio.core.data.source.remote.response.GarageResponse
import com.sstudio.core.data.source.remote.response.SalonResponse
import com.sstudio.core.data.source.remote.response.UserResponse
import com.sstudio.core.domain.model.Booking
import com.sstudio.core.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class RemoteDataSource(private val db: FirebaseFirestore) {

    fun getUser(userPhone: String): Flow<ApiResponse<UserResponse>> =
        flow<ApiResponse<UserResponse>> {
            val docRef = db.collection("User").document(userPhone)
            val user = docRef.get().await()
            if (user.exists())
                emit(ApiResponse.Success(user.toObject(UserResponse::class.java) ?: UserResponse()))
            else
                emit(ApiResponse.Empty)
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun setUser(user: User): Flow<ApiResponse<String>> =
        flow<ApiResponse<String>> {
            val docRef = db.collection("User").document(user.phoneNumber)
                .set(user).await()
            emit(ApiResponse.Success(""))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getHomeBanner(): Flow<ApiResponse<List<BannerResponse>>> =
        flow<ApiResponse<List<BannerResponse>>> {
            val docRef = db.collection("Banner")
            val banner = docRef.get().await()
            val listBanner = ArrayList<BannerResponse>()
            for (bnr in banner) {
                listBanner.add(bnr.toObject(BannerResponse::class.java))
            }
            emit(ApiResponse.Success(listBanner))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getHomeLookBook(): Flow<ApiResponse<List<BannerResponse>>> =
        flow<ApiResponse<List<BannerResponse>>> {
            val docRef = db.collection("LookBook")
            val banner = docRef.get().await()
            val listBanner = ArrayList<BannerResponse>()
            for (bnr in banner) {
                listBanner.add(bnr.toObject(BannerResponse::class.java))
            }
            emit(ApiResponse.Success(listBanner))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getAllGarage(): Flow<Resource<List<GarageResponse>>> {
        return flow { }
    }

    fun getGarage(id: String): Flow<Resource<GarageResponse>> {
        return flow { }
    }

    fun getAllSalon(): Flow<Resource<List<SalonResponse>>> {
        return flow { }
    }

    fun getSalon(id: String): Flow<Resource<SalonResponse>> {
        return flow { }
    }

    fun setBooking(booking: Booking) {
    }
}

