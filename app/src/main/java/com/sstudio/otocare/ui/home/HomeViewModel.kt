package com.sstudio.otocare.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.Banner
import com.sstudio.core.domain.model.Booking
import com.sstudio.core.domain.model.Cart
import com.sstudio.core.domain.model.User
import com.sstudio.core.domain.usecase.OtoCareUseCase

class HomeViewModel(private val otoCareUseCase: OtoCareUseCase) : ViewModel() {

    var userPhone = ""

    var currentUserAuth: FirebaseUser? = FirebaseAuth.getInstance().currentUser.also {
        if (it?.phoneNumber != null)
            userPhone = it.phoneNumber ?: ""
    }

    fun setUser(user: User): LiveData<Resource<String>> =
        otoCareUseCase.setUser(user).asLiveData()

    fun getBookingInformation(): LiveData<Resource<Booking?>> =
        otoCareUseCase.getBookingInformation(userPhone).asLiveData()

    fun setCancelBooking(activeBookingId: String): LiveData<Resource<String>> =
        otoCareUseCase.setCancelBooking(activeBookingId).asLiveData()

    var getUser: LiveData<Resource<User>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                field = otoCareUseCase.getUser(userPhone).asLiveData()
            }
            return field
        }
        private set

    var getHomeBanner: LiveData<Resource<List<Banner>>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                field = otoCareUseCase.getHomeBanner().asLiveData()
            }
            return field
        }
        private set

    var getHomeLookBook: LiveData<Resource<List<Banner>>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                field = otoCareUseCase.getHomeLookBook().asLiveData()
            }
            return field
        }
        private set

    fun getCart(): LiveData<Resource<List<Cart>>> =
        otoCareUseCase.getCart(userPhone).asLiveData()
}