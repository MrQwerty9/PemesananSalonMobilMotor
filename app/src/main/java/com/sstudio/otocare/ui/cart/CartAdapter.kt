package com.sstudio.otocare.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sstudio.core.domain.model.Cart
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.ItemCartBinding

class CartAdapter :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var packageList: List<Cart> = listOf()
    private lateinit var context: Context
    var itemSelected = ArrayList<String>()
    var cartSelectedChange: ((ArrayList<String>) -> Unit)? = null
    var cartPlusClick: ((Cart) -> Unit)? = null
    var cartMinusClick: ((Cart) -> Unit)? = null
    var cartDeleteClick: ((Cart) -> Unit)? = null

    fun setCart(list: List<Cart>) {
        this.packageList = list
        notifyDataSetChanged()
    }

    fun setSelectedPositions(positions: ArrayList<String>) {
        itemSelected = positions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        val itemView =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return packageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(packageList[position])
    }

    inner class ViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: Cart) {
            binding.tvProductName.text = cart.product.name
            binding.tvProductPrice.text = "Rp.${cart.product.price}"
            binding.etCartQuantity.setText(cart.qty.toString())

            try {
                Picasso.get().load(cart.product.image).into(binding.ivCart)
            } catch (e: Exception) {

            }


            if (cart.qty == 1) {
                binding.btnCartMinus.isEnabled = false
                binding.btnCartMinus.setImageResource(R.drawable.ic_outline_minus_circle_disabled)
            }
            if (itemSelected.contains(cart.product.id)) {
                binding.cbCart.isChecked = true
            }

            binding.cbCart.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    itemSelected.add(cart.product.id)
                } else {
                    itemSelected.remove(cart.product.id)
                }

                cartSelectedChange?.invoke(itemSelected)
            }

            binding.btnCartMinus.setOnClickListener {
                cartMinusClick?.invoke(cart)
            }

            binding.btnCartPlus.setOnClickListener {
                cartPlusClick?.invoke(cart)
            }

            binding.btnCartDelete.setOnClickListener {
                cartDeleteClick?.invoke(cart)
            }
        }
    }
}