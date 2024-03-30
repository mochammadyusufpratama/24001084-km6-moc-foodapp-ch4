package com.app.makanku.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.makanku.data.model.Product
import com.app.makanku.databinding.ItemProductBinding
import com.app.makanku.utils.indonesianCurrency

class ProductListAdapter(private val itemClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductListAdapter.ItemProductViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(
                    oldItem: Product,
                    newItem: Product
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Product,
                    newItem: Product
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    fun submitData(data: List<Product>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemProductViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: ItemProductViewHolder, position: Int) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class ItemProductViewHolder(
        private val binding: ItemProductBinding,
        val itemClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Product) {
            with(item) {
                binding.ivImageProduct.load(item.imgUrl) {
                    crossfade(true)
                }
                binding.tvProductName.text = item.name
                binding.tvProductPrice.text = item.price.indonesianCurrency()
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

}