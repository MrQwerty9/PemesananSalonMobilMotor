package com.sstudio.otocare.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.User
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.ActivityHomeBinding
import com.sstudio.otocare.databinding.LayoutUpdateInformationBinding
import com.sstudio.otocare.ui.login.LoginActivity
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel


class HomeActivity : AppCompatActivity() {

    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var dialog: AlertDialog
    private var isLogin = false
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModel()

    companion object {
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()

        dialog.show()
        checkCurrentUser()

        val navController = supportFragmentManager
            .findFragmentById(R.id.fragment_nav_host) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNav,
            navController.navController
        )
    }

    private fun checkCurrentUser() {
        val userPhone = viewModel.currentUserAuth.phoneNumber
        if (userPhone != null) {
            isLogin = true

            viewModel.getUser?.observe(this) { resource ->
                when (resource) {
                    is Resource.Loading -> dialog.show()
                    is Resource.Success -> {
                        dialog.dismiss()
                        resource.data?.let {
                            currentUser = it
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
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
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
                        resource.data?.let {
                            currentUser = user
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




















