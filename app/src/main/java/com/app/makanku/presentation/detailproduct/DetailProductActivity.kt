package com.app.makanku.presentation.detailproduct

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.app.makanku.R
import com.app.makanku.data.model.Menu
import com.app.makanku.databinding.ActivityDetailProductBinding
import com.app.makanku.utils.indonesianCurrency
import com.app.makanku.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailProductActivity : AppCompatActivity() {
    private val binding: ActivityDetailProductBinding by lazy {
        ActivityDetailProductBinding.inflate(layoutInflater)
    }

    private val detailProductViewModel: DetailProductViewModel by viewModel {
        parametersOf(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindProduct(detailProductViewModel.product)
        setClickListener()
        observeData()

        binding.tvNominal.text = "1"

        if (binding.tvNominal.text.toString().toInt() == 1) {
            binding.ivMinusBtn.alpha = 0.3f
        }
    }

    private fun setClickListener() {
        binding.ivBackBtn.setOnClickListener {
            onBackPressed()
        }

        binding.ivMinusBtn.setOnClickListener {
            var nominal = binding.tvNominal.text.toString().toInt()

            if (nominal == 2) {
                binding.ivMinusBtn.alpha = 0.3f
            }

            if (nominal > 1) {
                detailProductViewModel.minus()
            }
        }

        binding.ivPlusBtn.setOnClickListener {
            var nominal = binding.tvNominal.text.toString().toInt()

            if (nominal > 0) {
                binding.ivMinusBtn.alpha = 1.0f
                detailProductViewModel.add()
            }
        }

        binding.btnAddToCart.setOnClickListener {
            addProductToCart()
        }
    }

    private fun addProductToCart() {
        detailProductViewModel.addToCart().observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(
                        this,
                        getString(R.string.text_add_to_cart_success),
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(this, getString(R.string.add_to_cart_failed), Toast.LENGTH_SHORT)
                        .show()
                },
                doOnLoading = {
                    Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_SHORT).show()
                },
            )
        }
    }

    private fun bindProduct(menu: Menu?) {
        menu?.let { item ->
            binding.ivProductImage.load(item.imageUrl) {
                crossfade(true)
            }
            binding.tvProductName.text = item.name
            binding.tvProductDesc.text = item.desc
            binding.tvProductPrice.text = item.price.indonesianCurrency()
            binding.tvProductLocation.text = item.locationAddress
        }
    }

    private fun observeData() {
        detailProductViewModel.priceLiveData.observe(this) {
            binding.btnAddToCart.isEnabled = it != 0.0
            binding.tvBtnText.text = getString(R.string.text_add_to_cart, it.indonesianCurrency())
        }
        detailProductViewModel.productCountLiveData.observe(this) {
            binding.tvNominal.text = it.toString()
        }
    }

    private fun navigateToGoogleMaps(menu: Menu?) {
        menu?.let { item ->
            binding.tvProductLocation.setOnClickListener {
                openGoogleMaps(item.locationUrl)
            }
        }
    }

    private fun openGoogleMaps(it: String) {
        val gmmIntentUri = Uri.parse(it)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)
    }

    companion object {
        const val EXTRA_PRODUCT = "EXTRA_PRODUCT"

        fun startActivity(
            context: Context,
            menu: Menu,
        ) {
            val intent = Intent(context, DetailProductActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT, menu)
            context.startActivity(intent)
        }
    }
}
