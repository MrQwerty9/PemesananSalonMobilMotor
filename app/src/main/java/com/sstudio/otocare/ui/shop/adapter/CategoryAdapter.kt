package com.sstudio.otocare.ui.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.core.domain.model.CategoryProduct
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var itemSelected = CategoryProduct()
    var selectedCategoryPosition: ((Int) -> Unit)? = null

    private var timeSlotList = ArrayList<CategoryProduct>()
    private lateinit var context: Context
    private var selectedPosition = 0

    fun setCategory(list: List<CategoryProduct>) {
        timeSlotList = list as ArrayList<CategoryProduct>
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        val itemView =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return timeSlotList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timeSlotList[position], position)
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryProduct: CategoryProduct, position: Int) {
            binding.tvCategory.text = if (categoryProduct.id == 0)
                context.getString(R.string.all)
            else
                categoryProduct.name

            if (selectedPosition == position) {
                itemSelected = timeSlotList[selectedPosition]
                setItemSelected(binding)
            } else { //unselect
                setItemUnselected(binding)
            }

            binding.cvCategory.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                selectedCategoryPosition?.invoke(selectedPosition)
            }
        }

        private fun setItemSelected(binding: ItemCategoryBinding) {
            binding.tvCategory.setBackgroundColor(
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
            binding.tvCategory.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        private fun setItemUnselected(binding: ItemCategoryBinding) {
            binding.tvCategory.setBackgroundColor(
                ContextCompat.getColor(context, R.color.light_grey)
            )
            binding.tvCategory.setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
        }
    }

}