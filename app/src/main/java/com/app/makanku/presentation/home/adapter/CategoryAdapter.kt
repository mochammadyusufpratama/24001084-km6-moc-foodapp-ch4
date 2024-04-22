package com.app.makanku.presentation.home.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.makanku.R
import com.app.makanku.data.model.Category
import com.app.makanku.databinding.ItemCategoryBinding

class CategoryAdapter(private val itemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedCategory = RecyclerView.NO_POSITION
    private var onItemClickListener: ((Category) -> Unit)? = null

    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }

    fun submitData(items: List<Category>) {
        asyncDataDiffer.submitList(items)
    }

    private val asyncDataDiffer = AsyncListDiffer<Category>(
        this, object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(asyncDataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncDataDiffer.currentList.size

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentCategory = asyncDataDiffer.currentList[position]
                    if (selectedCategory != position) {
                        val previouslySelectedCategory = selectedCategory
                        selectedCategory = position
                        notifyItemChanged(previouslySelectedCategory)
                        notifyItemChanged(selectedCategory)
                        onItemClickListener?.invoke(currentCategory)
                    } else {
                        selectedCategory = RecyclerView.NO_POSITION
                        notifyItemChanged(position)
                        onItemClickListener?.invoke(currentCategory)
                    }
                }
            }
        }

        fun bind(item: Category) {
            binding.tvCategoryName.text = item.name
            binding.ivImageCategory.load(item.imgUrl) {
                crossfade(true)
            }
            if (adapterPosition == selectedCategory) {
//                binding.tvCategoryName.setBackgroundResource(R.drawable.bg_rounded)
                binding.tvCategoryName.apply {
                    setBackgroundResource(R.drawable.bg_rounded)
                }
            } else {
                binding.tvCategoryName.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

}