package com.sstudio.otocare

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.User
import com.sstudio.otocare.databinding.ActivityMainBinding
import com.sstudio.otocare.databinding.LayoutUpdateInformationBinding
import com.sstudio.otocare.ui.home.HomeViewModel
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var dialog: AlertDialog
    private var isLogin = false
    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModel()

    companion object {
        var currentUser: MutableLiveData<User> = MutableLiveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()

        dialog.show()
        checkCurrentUser()

        if (savedInstanceState == null) {
            val navController: NavController = findNavController(R.id.fragment_nav_host)
            NavigationUI.setupWithNavController(
                binding.bottomNav,
                navController
            )
        }
    }

    private fun checkCurrentUser() {
        viewModel.currentUserAuth
        val userPhone = viewModel.userPhone
        if (userPhone != "") {
            isLogin = true

            viewModel.getUser?.observe(this) { resource ->
                when (resource) {
                    is Resource.Loading -> dialog.show()
                    is Resource.Success -> {
                        dialog.dismiss()
                        resource.data?.let {
                            currentUser.value = it
                            if (it == User()) {
                                showUpdateDialog(userPhone)
                            }
                        }
                    }
                    is Resource.Error -> {
                        dialog.dismiss()
//                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        dialog.dismiss()
    }

    private fun showUpdateDialog(phoneNumber: String) {

        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setTitle("Satu langkah lagi!")
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setCancelable(false)
        val sheetViewBinding = LayoutUpdateInformationBinding.inflate(layoutInflater, null, false)
        bottomSheetDialog.setContentView(sheetViewBinding.root)
        bottomSheetDialog.show()

        sheetViewBinding.btnUpdate.setOnClickListener {
            if (!dialog.isShowing)
                dialog.show()
            val user = User(
                sheetViewBinding.etName.text.toString(),
                sheetViewBinding.etAddress.text.toString(),
                phoneNumber
            )
            viewModel.setUser(user).observe(this) { resource ->
                when (resource) {
                    is Resource.Loading -> dialog.show()
                    is Resource.Success -> {
                        dialog.dismiss()
                        bottomSheetDialog.dismiss()
                        resource.data?.let {
                            currentUser.value = user
                        }
                    }
                    is Resource.Error -> {
                        dialog.dismiss()
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}




















