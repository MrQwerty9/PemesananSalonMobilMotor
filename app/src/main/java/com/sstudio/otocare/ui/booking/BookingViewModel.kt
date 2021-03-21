package com.sstudio.otocare.ui.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.Banner
import com.sstudio.core.domain.model.User
import com.sstudio.core.domain.usecase.OtoCareUseCase

class BookingViewModel(private val otoCareUseCase: OtoCareUseCase) : ViewModel() {

    var userPhone = ""

    var currentUserAuth = FirebaseAuth.getInstance().currentUser.also {
        if (it?.phoneNumber != null)
            userPhone = it.phoneNumber ?: ""
    }

    fun setUser(user: User): LiveData<Resource<String>> =
        otoCareUseCase.setUser(user).asLiveData()

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
}