package com.sstudio.otocare.ui.booking

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.*
import com.sstudio.core.domain.usecase.OtoCareUseCase

class BookingViewModel(private val otoCareUseCase: OtoCareUseCase) : ViewModel() {

    var userPhone = ""
    var city = MutableLiveData<String>()
    var savedStateSpinnerCity = 0
    var savedStateItemGarage = -1

    var currentUserAuth = FirebaseAuth.getInstance().currentUser.also {
        if (it?.phoneNumber != null)
            userPhone = it.phoneNumber ?: ""
    }

    fun setUser(user: User): LiveData<Resource<String>> =
        otoCareUseCase.setUser(user).asLiveData()

    var getAllCityOfGarage: LiveData<Resource<List<City>>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                field = otoCareUseCase.getAllCityOfGarage().asLiveData()
            }
            return field
        }
        private set

    val getBranchOfCity: LiveData<Resource<List<Garage>>> =
        Transformations.switchMap(city) {
            otoCareUseCase.getBranchOfCity(it).asLiveData()
        }

    var getPackage: LiveData<Resource<List<Package>>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                field = otoCareUseCase.getAllPackage().asLiveData()
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