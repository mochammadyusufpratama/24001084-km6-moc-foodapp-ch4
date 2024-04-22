package com.app.makanku.presentation.home.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.app.makanku.core.ViewHolderBinder
import com.app.makanku.data.model.Menu
import com.app.makanku.databinding.ItemMenuGridBinding
import com.app.makanku.utils.indonesianCurrency

class MenuGridViewHolder(
    private val binding: ItemMenuGridBinding,
    private val listener: MenuAdapter.OnItemClickedListener<Menu>
) : ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        item.let {
            binding.ivMenuImage.load(it.imageUrl)
            binding.tvMenuName.text = it.name
            binding.tvMenuPrice.text = it.price.indonesianCurrency()
            itemView.setOnClickListener{
                listener.onItemSelected(item)
            }
        }
    }
}