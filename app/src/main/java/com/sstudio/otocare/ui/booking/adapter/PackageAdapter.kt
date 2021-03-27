package com.sstudio.otocare.ui.booking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sstudio.core.domain.model.Package
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.ItemPackageBinding

class PackageAdapter :
    RecyclerView.Adapter<PackageAdapter.ViewHolder>() {

    private val cardViewList: ArrayList<CardView> = ArrayList()
    private var packageList: List<Package> = listOf()
    private var selectedPosition = -1
    private lateinit var context: Context
    var itemSelected: Package? = null
    var selectedPackagePosition: ((Int) -> Unit)? = null

    fun setPackage(list: List<Package>) {
        this.packageList = list
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        val itemView =
            ItemPackageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return packageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(packageList[position])
    }

    inner class ViewHolder(private val binding: ItemPackageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pkg: Package) {
            binding.tvPackageName.text = pkg.name
            binding.tvPackagePrice.text = "Rp.${pkg.price}"
            Picasso.get().load(pkg.image).into(binding.imageView)
            if (pkg.popular)
                binding.tvPopular.visibility = View.VISIBLE

            if (selectedPosition == position) {
                itemSelected = packageList[selectedPosition]
                binding.cardPackages.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red_light
                    )
                )
            } else {
                binding.cardPackages.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.light_grey
                    )
                )
            }


            binding.cardPackages.setOnClickListener {
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition)
                selectedPosition = position
                notifyItemChanged(selectedPosition)
                selectedPackagePosition?.invoke(selectedPosition)
            }
        }
    }
}