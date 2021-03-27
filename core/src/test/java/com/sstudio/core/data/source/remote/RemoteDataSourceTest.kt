package com.sstudio.core.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.sstudio.core.data.source.remote.network.ApiResponse
import com.sstudio.core.data.source.remote.response.GarageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {

    lateinit var db: FirebaseFirestore

    @Before
    fun setup() {
        db = FirebaseFirestore.getInstance()
    }

    fun testGetUser() {}

    fun testSetUser() {}

    fun testGetHomeBanner() {}

    fun testGetHomeLookBook() {}

    fun testGetAllTeam() {}

    fun testGetTeam() {}

    @Test
    fun testGetAllGarage() {
        flow<ApiResponse<List<GarageResponse>>> {
            val docRef = db.collection("Garage")
            val garage = docRef.get().await()
            val listGarage = ArrayList<GarageResponse>()
            print(listGarage)
            for (grg in garage) {
                listGarage.add(grg.toObject(GarageResponse::class.java))
            }
            emit(ApiResponse.Success(listGarage))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun testGetGarage() {}

    fun testSetBooking() {}
}