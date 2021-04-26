package com.sstudio.otocare.ui.cart

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.Cart
import com.sstudio.core.domain.model.User
import com.sstudio.otocare.MainActivity
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.ActivityCartBinding
import com.sstudio.otocare.ui.booking.BookingActivity
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel


class CartActivity : AppCompatActivity() {

    private var alreadyBooking: Boolean = false
    private val viewModel: CartViewModel by viewModel()
    lateinit var dialog: AlertDialog
    private lateinit var binding: ActivityCartBinding
    private val cartAdapter = CartAdapter()
    private var user: User? = null
    private var cart: ArrayList<Cart> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.cart)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()

        getCurrentUser()
        initView()
        getCart()
        checkActiveBooking()

        cartAction()

        binding.btnBooking.setOnClickListener {
            if (!alreadyBooking && user != null) {
                val intent = Intent(this, BookingActivity::class.java)
                intent.putExtra(BookingActivity.EXTRA_USER, user)
                intent.putExtra(BookingActivity.EXTRA_CART, cart)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.already_booking_your_account),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun cartAction() {
        cartAdapter.cartMinusClick = {
            it.qty -= 1
            viewModel.setCart(it).observe(this) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        dialog.show()
                    }
                    is Resource.Success -> {
                        dialog.dismiss()
                    }
                    is Resource.Error -> {
                        dialog.dismiss()
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        cartAdapter.cartPlusClick = {
            it.qty += 1
            viewModel.setCart(it).observe(this) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        dialog.show()
                    }
                    is Resource.Success -> {
                        dialog.dismiss()
                    }
                    is Resource.Error -> {
                        dialog.dismiss()
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        cartAdapter.cartDeleteClick = {
            viewModel.deleteCart(it).observe(this) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        dialog.show()
                    }
                    is Resource.Success -> {
                        dialog.dismiss()
                    }
                    is Resource.Error -> {
                        dialog.dismiss()
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getCurrentUser() {
        MainActivity.currentUser.observe(this) {
            user = it
        }
    }

    private fun checkActiveBooking() {
        viewModel.getBookingInformation().observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    alreadyBooking = resource.data != null
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCart() {
        viewModel.getCart().observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    dialog.show()
                }
                is Resource.Success -> {
                    dialog.dismiss()
                    Log.d("mytag", "${resource.data}")
                    resource.data?.let {
                        if (it.isNotEmpty()) {
                            cart = it as ArrayList<Cart>
                            binding.rvCart.adapter = cartAdapter
                            cartAdapter.setCart(it)
                        }
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initView() {
        binding.rvCart.setHasFixedSize(true)
        binding.rvCart.layoutManager = GridLayoutManager(this, 1)

        cartAdapter.cartSelectedChange = {
            viewModel.savedStateCartSelected = it
        }
        cartAdapter.setSelectedPositions(viewModel.savedStateCartSelected)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}