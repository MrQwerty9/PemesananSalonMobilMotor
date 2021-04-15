package com.sstudio.otocare.ui.shop


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sstudio.core.data.Resource
import com.sstudio.otocare.databinding.FragmentShoppingBinding
import com.sstudio.otocare.ui.shop.adapter.CategoryAdapter
import com.sstudio.otocare.ui.shop.adapter.ProductAdapter
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel

class ShoppingFragment : Fragment() {

    lateinit var dialog: AlertDialog
    private val viewModel: ShopViewModel by viewModel()
    private var _binding: FragmentShoppingBinding? = null
    private val binding get() = _binding!!
    private val productAdapter = ProductAdapter()
    private val categoryAdapter = CategoryAdapter()
    private var category = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = SpotsDialog.Builder().setContext(activity).setCancelable(false).build()

        loadCategory()
        loadProducts(category)

        categoryAdapter.selectedCategoryPosition = {
            category = it
            loadProducts(category)
        }

        productAdapter.itemClicked = {
            setCart(it.id)
        }
    }

    private fun loadCategory() {
        viewModel.getCategory?.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
                        binding.rvCategory.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        binding.rvCategory.adapter = categoryAdapter
                        categoryAdapter.setCategory(it)
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadProducts(category: Int) {
        viewModel.getProduct(category).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
                        productAdapter.setProduct(it)
                        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
                        binding.rvProducts.adapter = productAdapter
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setCart(productId: String) {
        viewModel.currentUserAuth
        viewModel.setCart(productId).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
                        Toast.makeText(
                            requireContext(),
                            "Berhasil ditambah ke keranjang",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
