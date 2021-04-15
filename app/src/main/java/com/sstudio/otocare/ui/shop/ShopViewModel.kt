package com.sstudio.otocare.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.CategoryProduct
import com.sstudio.core.domain.model.Product
import com.sstudio.core.domain.usecase.OtoCareUseCase

class ShopViewModel(private val otoCareUseCase: OtoCareUseCase) : ViewModel() {

    var userPhone = ""

    var currentUserAuth: FirebaseUser? = FirebaseAuth.getInstance().currentUser.also {
        if (it?.phoneNumber != null)
            userPhone = it.phoneNumber ?: ""
    }

    fun getProduct(category: Int): LiveData<Resource<List<Product>>> =
        otoCareUseCase.getProduct(category).asLiveData()

    var getCategory: LiveData<Resource<List<CategoryProduct>>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                field = otoCareUseCase.getCategoryProduct().asLiveData()
            }
            return field
        }
        private set

    fun setCart(productId: String): LiveData<Resource<String>> =
        otoCareUseCase.setCart(userPhone, productId).asLiveData()
}