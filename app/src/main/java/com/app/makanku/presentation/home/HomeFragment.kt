package com.app.makanku.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import com.app.makanku.R
import com.app.makanku.data.datasource.category.DummyCategoryDataSource
import com.app.makanku.data.datasource.product.DummyProductDataSource
import com.app.makanku.data.model.Category
import com.app.makanku.data.model.Product
import com.app.makanku.data.repository.CategoryRepository
import com.app.makanku.data.repository.CategoryRepositoryImpl
import com.app.makanku.data.repository.ProductRepository
import com.app.makanku.data.repository.ProductRepositoryImpl
import com.app.makanku.databinding.FragmentHomeBinding
import com.app.makanku.presentation.detailproduct.DetailProductActivity
import com.app.makanku.presentation.home.adapter.CategoryListAdapter
import com.app.makanku.presentation.home.adapter.ProductListAdapter
import com.app.makanku.utils.GenericViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels {
        val productDataSource = DummyProductDataSource()
        val productRepository: ProductRepository = ProductRepositoryImpl(productDataSource)
        val categoryDataSource = DummyCategoryDataSource()
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl(categoryDataSource)
        GenericViewModelFactory.create(HomeViewModel(categoryRepository, productRepository))
    }

    private val categoryAdapter: CategoryListAdapter by lazy {
        CategoryListAdapter {

        }
    }

    private val productAdapter: ProductListAdapter by lazy {
        ProductListAdapter {
            DetailProductActivity.startActivity(requireContext(), it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindCategoryList(viewModel.getCategories())
        bindProductList(viewModel.getProducts())
        binding.layoutBanner.ivBanner.load("https://github.com/mochammadyusufpratama/makanku-asset/blob/main/bg_banner.jpg?raw=true")
    }

    private fun bindCategoryList(data: List<Category>) {
        binding.rvCategory.apply {
            adapter = categoryAdapter
        }
        categoryAdapter.submitData(data)
    }

    private fun bindProductList(data: List<Product>) {
        binding.rvProduct.apply {
            adapter = productAdapter
        }
        productAdapter.submitData(data)
    }

}