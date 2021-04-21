package com.sstudio.otocare.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.Booking
import com.sstudio.core.domain.model.Cart
import com.sstudio.core.domain.model.User
import com.sstudio.core.domain.usecase.OtoCareUseCase

class CartViewModel(private val otoCareUseCase: OtoCareUseCase) : ViewModel() {

    var userPhone = ""
    var savedStateCartSelected = ArrayList<String>()

    var currentUserAuth: FirebaseUser? = FirebaseAuth.getInstance().currentUser.also {
        if (it?.phoneNumber != null)
            userPhone = it.phoneNumber ?: ""
    }

    var getUser: LiveData<Resource<User>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                field = otoCareUseCase.getUser(userPhone).asLiveData()
            }
            return field
        }
        private set

    fun getCart(): LiveData<Resource<List<Cart>>> =
        otoCareUseCase.getCart(userPhone).asLiveData()

    fun setCart(cart: Cart): LiveData<Resource<String>> =
        otoCareUseCase.setCart(userPhone, cart).asLiveData()

    fun getBookingInformation(): LiveData<Resource<Booking?>> =
        otoCareUseCase.getBookingInformation(userPhone).asLiveData()

    fun deleteCart(cart: Cart): LiveData<Resource<String>> =
        otoCareUseCase.deleteCart(userPhone, cart).asLiveData()
}