package com.app.makanku.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.makanku.R
import com.app.makanku.core.ViewHolderBinder
import com.app.makanku.data.model.Cart
import com.app.makanku.databinding.ItemCartProductBinding
import com.app.makanku.databinding.ItemCartProductOrderBinding
import com.app.makanku.utils.doneEditing
import com.app.makanku.utils.indonesianCurrency

class CartListAdapter(private val cartListener: CartListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Cart>() {
                override fun areItemsTheSame(
                    oldItem: Cart,
                    newItem: Cart,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Cart,
                    newItem: Cart,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<Cart>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return if (cartListener != null) {
            CartViewHolder(
                ItemCartProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                cartListener,
            )
        } else {
            CartOrderViewHolder(
                ItemCartProductOrderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as ViewHolderBinder<Cart>).bind(dataDiffer.currentList[position])
    }
}

class CartViewHolder(
    private val binding: ItemCartProductBinding,
    private val cartListener: CartListener?,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
        setClickListeners(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            binding.ivProductImage.load(item.menuImgUrl) {
                crossfade(true)
            }
            tvProductCount.text = item.itemQuantity.toString()
            tvProductName.text = item.menuName
            tvProductPrice.text = (item.itemQuantity * item.menuPrice).indonesianCurrency()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.etNotesItem.setText(item.itemNotes)
        binding.etNotesItem.doneEditing {
            binding.etNotesItem.clearFocus()
            val newItem =
                item.copy().apply {
                    itemNotes = binding.etNotesItem.text.toString().trim()
                }
            cartListener?.onUserDoneEditingNotes(newItem)
        }
    }

    private fun setClickListeners(item: Cart) {
        with(binding) {
            ivMinus.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item) }
            ivPlus.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item) }
            ivRemoveCart.setOnClickListener { cartListener?.onRemoveCartClicked(item) }
        }
    }
}

class CartOrderViewHolder(
    private val binding: ItemCartProductOrderBinding,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            binding.ivProductImage.load(item.menuImgUrl) {
                crossfade(true)
            }
            tvTotalQuantity.text =
                itemView.rootView.context.getString(
                    R.string.total_quantity,
                    item.itemQuantity.toString(),
                )
            tvProductName.text = item.menuName
            tvProductPrice.text = item.menuPrice.indonesianCurrency()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.tvNotes.text = item.itemNotes
    }
}

interface CartListener {
    fun onPlusTotalItemCartClicked(cart: Cart)

    fun onMinusTotalItemCartClicked(cart: Cart)

    fun onRemoveCartClicked(cart: Cart)

    fun onUserDoneEditingNotes(cart: Cart)
}
