package com.app.makanku.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.app.makanku.R
import com.app.makanku.data.model.Category
import com.app.makanku.data.model.Menu
import com.app.makanku.databinding.FragmentHomeBinding
import com.app.makanku.presentation.detailproduct.DetailProductActivity
import com.app.makanku.presentation.home.adapter.CategoryAdapter
import com.app.makanku.presentation.home.adapter.MenuAdapter
import com.app.makanku.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModel()

    private var categories: List<Category>? = null
    private var menuAdapter: MenuAdapter? = null
    private var menuItems: List<Menu>? = null
    private var lastSelectedCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutBanner.ivBanner.load("https://github.com/mochammadyusufpratama/makanku-asset/blob/main/bg_banner.jpg?raw=true")
        val isUsingGridMode = homeViewModel.isUsingGridMode()
        bindModeList(isUsingGridMode)
        setClickAction(isUsingGridMode)
        setIconFromPref(isUsingGridMode)
        setCategoryData()
        loadCategoriesData()
        loadMenuData()
        setDisplayName()
        setListMenuOnCategoryClicked()
    }

    private fun setDisplayName() {
        if (!homeViewModel.userIsLoggedIn()) {
            binding.layoutHeader.layoutHeader.tvName.apply {
                text = getString(R.string.text_display_name, "John Doe")
            }
        } else {
            val currentUser = homeViewModel.getCurrentUser()
            binding.layoutHeader.layoutHeader.tvName.text =
                getString(R.string.text_display_name, currentUser?.fullName)
        }
    }

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter {}
    }

    private fun loadCategoriesData() {
        categories?.let { bindCategory(it) } ?: getCategoryData()
    }

    private fun loadMenuData() {
        menuItems?.let { bindMenu(it) } ?: getMenuData()
    }

    private fun bindCategory(categories: List<Category>) {
        this.categories = categories
        categoryAdapter.submitData(categories)
    }

    private fun bindMenu(menu: List<Menu>) {
        this.menuItems = menu
        menuAdapter?.submitData(menu)
    }

    private fun setCategoryData() {
        binding.rvCategory.apply {
            adapter = categoryAdapter
        }
    }

    private fun pushToDetail(item: Menu) {
        DetailProductActivity.startActivity(
            requireContext(),
            item,
        )
    }

    private fun setListMenuOnCategoryClicked() {
        categoryAdapter.setOnItemClickListener { category ->
            if (category.name == lastSelectedCategory) {
                lastSelectedCategory = null
                getMenuData()
            } else {
                getMenuData(category.name)
                lastSelectedCategory = category.name
            }
        }
    }

    private fun getCategoryData() {
        homeViewModel.getCategories().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.rvCategory.isVisible = true
                    it.payload?.let { data ->
                        categories = data
                        bindCategory(data)
                    }
                },
                doOnError = {
                },
                doOnEmpty = {
                },
                doOnLoading = {
                },
            )
        }
    }

    private fun getMenuData(categoryName: String? = null) {
        homeViewModel.getMenu(categoryName).observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.rvProduct.isVisible = true
                    it.payload?.let { data ->
                        menuItems = data
                        bindMenu(data)
                    }
                },
                doOnError = {
                },
                doOnEmpty = {
                },
                doOnLoading = {
                },
            )
        }
    }

    private fun bindModeList(isGridMode: Boolean) {
        val listMode =
            if (isGridMode) {
                MenuAdapter.MODE_GRID
            } else {
                MenuAdapter.MODE_LIST
            }
        menuAdapter =
            MenuAdapter(
                listMode = listMode,
                listener =
                    object : MenuAdapter.OnItemClickedListener<Menu> {
                        override fun onItemSelected(item: Menu) {
                            pushToDetail(item)
                        }

                        override fun onItemAddedToCart(item: Menu) {
                            homeViewModel.addItemToCart(item)
                        }
                    },
            )
        binding.rvProduct.apply {
            adapter = this@HomeFragment.menuAdapter
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    if (isGridMode) {
                        2
                    } else {
                        1
                    },
                )
        }
    }

    private fun setClickAction(usingGrid: Boolean) {
        var isGridMode = usingGrid
        binding.btnListToGrid.setOnClickListener {
            isGridMode = !isGridMode
            if (isGridMode) {
                binding.btnListToGrid.setImageResource(R.drawable.ic_list)
            } else {
                binding.btnListToGrid.setImageResource(R.drawable.ic_grid)
            }
            bindModeList(isGridMode)
            loadMenuData()
            homeViewModel.setUsingGridMode(isGridMode)
        }
    }

    private fun setIconFromPref(isGridMode: Boolean) {
        if (isGridMode) {
            binding.btnListToGrid.setImageResource(R.drawable.ic_list)
        } else {
            binding.btnListToGrid.setImageResource(R.drawable.ic_grid)
        }
    }
}
