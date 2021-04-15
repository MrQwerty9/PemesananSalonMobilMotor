package com.sstudio.otocare.ui.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sstudio.core.domain.model.Product
import com.sstudio.otocare.databinding.ItemProductBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var itemClicked: ((Product) -> Unit)? = null

    private var productList = ArrayList<Product>()
    private lateinit var context: Context

    fun setProduct(list: List<Product>) {
        productList = list as ArrayList<Product>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        val itemView =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvName.text = product.name
            binding.tvPrice.text = "Rp" + product.price.toInt().toString()
            Picasso.get().load(product.image).into(binding.ivProduct)
            binding.btnCart.setOnClickListener {
                itemClicked?.invoke(product)
            }
        }
    }

}