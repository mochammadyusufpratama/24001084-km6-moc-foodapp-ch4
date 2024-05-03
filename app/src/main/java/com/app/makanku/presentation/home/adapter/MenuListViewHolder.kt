package com.app.makanku.presentation.home.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.makanku.core.ViewHolderBinder
import com.app.makanku.data.model.Menu
import com.app.makanku.databinding.ItemMenuListBinding
import com.app.makanku.utils.indonesianCurrency

class MenuListViewHolder(
    private val binding: ItemMenuListBinding,
    private val listener: MenuAdapter.OnItemClickedListener<Menu>,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        item.let {
            binding.ivMenuImage.load(it.imageUrl)
            binding.tvMenuName.text = it.name
            binding.tvMenuPrice.text = it.price.indonesianCurrency()
            itemView.setOnClickListener {
                listener.onItemSelected(item)
            }
        }
    }
}
