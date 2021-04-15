package com.sstudio.core.data.source.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sstudio.core.data.source.remote.network.ApiResponse
import com.sstudio.core.data.source.remote.response.*
import com.sstudio.core.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class RemoteDataSource(private val db: FirebaseFirestore) {

    fun getUser(userPhone: String): Flow<ApiResponse<UserResponse>> {
        return flow<ApiResponse<UserResponse>> {
            val docRef = db.collection("User").document(userPhone)
            val user = docRef.get().await()
            if (user.exists())
                emit(ApiResponse.Success(user.toObject(UserResponse::class.java) ?: UserResponse()))
            else
                emit(ApiResponse.Empty)
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun setUser(user: User): Flow<ApiResponse<String>> =
        flow<ApiResponse<String>> {
            db.collection("User").document(user.phoneNumber)
                .set(user).await()
            emit(ApiResponse.Success(""))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getHomeBanner(): Flow<ApiResponse<List<BannerResponse>>> {
        return flow<ApiResponse<List<BannerResponse>>> {
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
    }

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

    fun getAllPackage(): Flow<ApiResponse<List<PackageResponse>>> {
        return flow<ApiResponse<List<PackageResponse>>> {
            val docRef = db.collection("Package")
            val packages = docRef.get().await()
            val list = ArrayList<PackageResponse>()
            for (pkg in packages) {
                val packageResponse = pkg.toObject(PackageResponse::class.java)
                packageResponse.id = pkg.id
                list.add(packageResponse)
//                Log.d("mytag", "remote ${packageResponse}")
            }

            emit(ApiResponse.Success(list))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun getAllCityOfGarage(): Flow<ApiResponse<List<CityResponse>>> {
        return flow<ApiResponse<List<CityResponse>>> {
            val docRef = db.collection("Garage")
            val city = docRef.get().await()
            val listCity = ArrayList<CityResponse>()
            for (cty in city) {
                val cityResponse = cty.toObject(CityResponse::class.java)
                cityResponse.id = cty.id
                listCity.add(cityResponse)
            }
//            Log.d("mytag", "remote ${city.get.}")
            emit(ApiResponse.Success(listCity))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getBranchOfCity(city: String): Flow<ApiResponse<List<GarageResponse>>> {
        return callbackFlow<ApiResponse<List<GarageResponse>>> {
            val docRef = db.collection("Garage").document(city).collection("Branch")
            val listener = docRef.addSnapshotListener { snapshot, exception ->
                val list = ArrayList<GarageResponse>()
                snapshot?.let {
                    for (grg in it) {
                        val response = grg.toObject(GarageResponse::class.java)
                        response.id = grg.id
                        list.add(response)
                    }
                }
                offer(ApiResponse.Success(list))

                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getWorkingHours(): Flow<ApiResponse<List<WorkingHoursResponse>>> {
        return callbackFlow<ApiResponse<List<WorkingHoursResponse>>> {
            val docRef = db.collection("WorkingHours")
            val listener = docRef.addSnapshotListener { snapshot, exception ->
                val listWorkHours = ArrayList<WorkingHoursResponse>()
                snapshot?.let {
                    for (hours in snapshot) {
                        val response = hours.toObject(WorkingHoursResponse::class.java)
                        response.id = hours.id.toInt()
                        listWorkHours.add(response)
                    }
                }
                offer(ApiResponse.Success(listWorkHours))

                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getTimeSlotBooked(date: String, garageId: String): Flow<ApiResponse<List<String>>> {
        return callbackFlow<ApiResponse<List<String>>> {
            val docRef = db.collection("Booking").whereEqualTo("date", date)
                .whereEqualTo("garageId", garageId)
            val listener = docRef.addSnapshotListener { snapshot, exception ->
                val listIdTimeSlot = ArrayList<String>()
                snapshot?.let {
                    for (timeSlot in it) {
                        listIdTimeSlot.add(timeSlot["timeId"].toString())
                    }
                }
                offer(ApiResponse.Success(listIdTimeSlot))

                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    fun setBooking(booking: BookingResponse): Flow<ApiResponse<String>> =
        flow<ApiResponse<String>> {
            db.collection("Booking")
                .add(booking).await()
            emit(ApiResponse.Success(""))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun setCancelBooking(activeBookingId: String): Flow<ApiResponse<String>> =
        flow<ApiResponse<String>> {
            db.collection("Booking").document(activeBookingId).delete().await()
            emit(ApiResponse.Success(""))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    fun getBookingUser(phoneNumber: String): Flow<ApiResponse<List<BookingResponse>>> {
        return callbackFlow<ApiResponse<List<BookingResponse>>> {
            val docRef = db.collection("Booking").whereEqualTo("userPhone", phoneNumber)
            val listener = docRef.addSnapshotListener { snapshot, exception ->
                val listBooking = ArrayList<BookingResponse>()
                snapshot?.let {
                    for (booking in it) {
                        val data = booking.toObject(BookingResponse::class.java)
                        data.id = booking.id
                        listBooking.add(data)
                    }
                }
                offer(ApiResponse.Success(listBooking))

                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getGarage(id: String): Flow<ApiResponse<GarageResponse>> {
        return flow<ApiResponse<GarageResponse>> {
            val docRef = db.collection("Garage").document(id)
            val snapshot = docRef.get().await()
            val response = snapshot.toObject(GarageResponse::class.java) ?: GarageResponse()
            emit(ApiResponse.Success(response))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun getPackage(id: String): Flow<ApiResponse<PackageResponse>> {
        return flow<ApiResponse<PackageResponse>> {
            val docRef = db.collection("Package").document(id)
            val snapshot = docRef.get().await()
            val response = snapshot.toObject(PackageResponse::class.java) ?: PackageResponse()
            emit(ApiResponse.Success(response))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun getCategoryProduct(): Flow<ApiResponse<List<CategoryProductResponse>>> {
        return flow<ApiResponse<List<CategoryProductResponse>>> {
            val docRef = db.collection("CategoryProduct")
            val snapshot = docRef.get().await()
            val list = ArrayList<CategoryProductResponse>()
            for (i in snapshot) {
                val data =
                    i.toObject(CategoryProductResponse::class.java)
                data.id = i.id
                list.add(data)
            }
            emit(ApiResponse.Success(list))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getAllProduct(): Flow<ApiResponse<List<ProductResponse>>> {
        return callbackFlow<ApiResponse<List<ProductResponse>>> {
            val docRef = db.collection("Product")
            val listener = docRef.addSnapshotListener { snapshot, exception ->
                val listBooking = ArrayList<ProductResponse>()
                snapshot?.let {
                    for (i in it) {
                        val data = i.toObject(ProductResponse::class.java)
                        data.id = i.id
                        listBooking.add(data)
                    }
                }
                offer(ApiResponse.Success(listBooking))

                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getProductByCategory(category: Int): Flow<ApiResponse<List<ProductResponse>>> {
        return callbackFlow<ApiResponse<List<ProductResponse>>> {
            val docRef = db.collection("Product").whereEqualTo("category", category.toString())
            val listener = docRef.addSnapshotListener { snapshot, exception ->
                val list = ArrayList<ProductResponse>()
                snapshot?.let {
                    for (i in it) {
                        val data = i.toObject(ProductResponse::class.java)
                        data.id = i.id
                        list.add(data)
                    }
                }
                offer(ApiResponse.Success(list))

                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getCart(userPhone: String): Flow<ApiResponse<CartResponse>> {
        return callbackFlow<ApiResponse<CartResponse>> {
            val docRef = db.collection("CartUser").document(userPhone)
            val listener = docRef.addSnapshotListener { snapshot, exception ->

                snapshot?.let {

                    val data = it.toObject(CartResponse::class.java)
                    if (data != null) {
                        offer(ApiResponse.Success(data))
                    } else {
                        offer(ApiResponse.Empty)
                    }
                }


                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    fun setCart(userPhone: String, productId: String): Flow<ApiResponse<String>> =
        flow<ApiResponse<String>> {
            val docRef = db.collection("CartUser").document(userPhone)
            if (!docRef.get().await().exists()) {
                docRef.set(CartResponse()).await() //create empty doc
            }
            docRef.update("productId", FieldValue.arrayUnion(productId)).await()
            emit(ApiResponse.Success(""))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}

