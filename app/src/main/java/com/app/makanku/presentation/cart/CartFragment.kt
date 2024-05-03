package com.app.makanku.presentation.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.makanku.R
import com.app.makanku.data.model.Cart
import com.app.makanku.databinding.FragmentCartBinding
import com.app.makanku.presentation.checkout.CheckoutActivity
import com.app.makanku.presentation.common.adapter.CartListAdapter
import com.app.makanku.presentation.common.adapter.CartListener
import com.app.makanku.utils.hideKeyboard
import com.app.makanku.utils.indonesianCurrency
import com.app.makanku.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    private val cartViewModel: CartViewModel by viewModel()

    private val adapter: CartListAdapter by lazy {

        CartListAdapter(
            object : CartListener {
                override fun onPlusTotalItemCartClicked(cart: Cart) {
                    cartViewModel.increaseCart(cart)
                }

                override fun onMinusTotalItemCartClicked(cart: Cart) {
                    cartViewModel.decreaseCart(cart)
                }

                override fun onRemoveCartClicked(cart: Cart) {
                    cartViewModel.removeCart(cart)
                }

                override fun onUserDoneEditingNotes(cart: Cart) {
                    cartViewModel.setCartNotes(cart)
                    hideKeyboard()
                }
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        observeData()
    }

    private fun setClickListeners() {
        binding.btnCheckout.setOnClickListener {
            startActivity(Intent(requireContext(), CheckoutActivity::class.java))
        }
    }

    private fun observeData() {
        cartViewModel.getAllCarts().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvCart.isVisible = false
                    binding.btnCheckout.alpha = 0.3f
                },
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvCart.isVisible = true
                    binding.btnCheckout.alpha = 1.0f
                    result.payload?.let { (carts, totalPrice) ->
                        // set list cart data
                        adapter.submitData(carts)
                        binding.tvTotalPrice.text = totalPrice.indonesianCurrency()
                    }
                    setClickListeners()
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = result.exception?.message.orEmpty()
                    binding.rvCart.isVisible = false
                    binding.btnCheckout.alpha = 0.3f
                },
                doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                    binding.rvCart.isVisible = false
                    result.payload?.let { (carts, totalPrice) ->
                        binding.tvTotalPrice.text = totalPrice.indonesianCurrency()
                    }
                    binding.btnCheckout.alpha = 0.3f
                },
            )
        }
    }

    private fun setupList() {
        binding.rvCart.adapter = this@CartFragment.adapter
    }
}
