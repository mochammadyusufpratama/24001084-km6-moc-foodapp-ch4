package com.app.makanku.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.app.makanku.R
import com.app.makanku.data.datasource.cart.CartDataSource
import com.app.makanku.data.datasource.cart.CartDatabaseDataSource
import com.app.makanku.data.datasource.category.CategoryApiDataSource
import com.app.makanku.data.datasource.category.CategoryDataSource
import com.app.makanku.data.datasource.menu.MenuApiDataSource
import com.app.makanku.data.datasource.menu.MenuDataSource
import com.app.makanku.data.datasource.user.AuthDataSource
import com.app.makanku.data.datasource.user.FirebaseAuthDataSource
import com.app.makanku.data.datasource.user.UserPrefDataSource
import com.app.makanku.data.datasource.user.UserPrefDataSourceImpl
import com.app.makanku.data.model.Category
import com.app.makanku.data.model.Menu
import com.app.makanku.data.repository.CartRepositoryImpl
import com.app.makanku.data.repository.CategoryRepository
import com.app.makanku.data.repository.CategoryRepositoryImpl
import com.app.makanku.data.repository.MenuRepository
import com.app.makanku.data.repository.MenuRepositoryImpl
import com.app.makanku.data.repository.UserPrefRepository
import com.app.makanku.data.repository.UserPrefRepositoryImpl
import com.app.makanku.data.repository.UserRepository
import com.app.makanku.data.repository.UserRepositoryImpl
import com.app.makanku.data.source.firebase.FirebaseService
import com.app.makanku.data.source.firebase.FirebaseServiceImpl
import com.app.makanku.data.source.local.database.AppDatabase
import com.app.makanku.data.source.local.pref.UserPreference
import com.app.makanku.data.source.local.pref.UserPreferenceImpl
import com.app.makanku.data.source.network.services.FoodAppApiService
import com.app.makanku.databinding.FragmentHomeBinding
import com.app.makanku.presentation.detailproduct.DetailProductActivity
import com.app.makanku.presentation.home.adapter.CategoryAdapter
import com.app.makanku.presentation.home.adapter.MenuAdapter
import com.app.makanku.utils.GenericViewModelFactory
import com.app.makanku.utils.proceedWhen

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val service: FirebaseService = FirebaseServiceImpl()
        val foodAppApiService = FoodAppApiService.invoke()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(database.cartDao())

        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)

        val menuDataSource: MenuDataSource = MenuApiDataSource(foodAppApiService)
        val menuRepository: MenuRepository = MenuRepositoryImpl(menuDataSource, userRepository)

        val categoryDataSource: CategoryDataSource = CategoryApiDataSource(foodAppApiService)
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl(categoryDataSource)

        val cartRepository = CartRepositoryImpl(cartDataSource)

        val userPrefPreference: UserPreference = UserPreferenceImpl(requireContext())
        val userPrefDataSource: UserPrefDataSource = UserPrefDataSourceImpl(userPrefPreference)
        val userPrefRepository: UserPrefRepository = UserPrefRepositoryImpl(userPrefDataSource)

        GenericViewModelFactory.create(
            HomeViewModel(
                categoryRepository,
                menuRepository,
                userRepository,
                cartRepository,
                userPrefRepository
            )
        )

    }

    private var categories: List<Category>? = null
    private var menuAdapter: MenuAdapter? = null
    private var menuItems: List<Menu>? = null
    private var lastSelectedCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutBanner.ivBanner.load("https://github.com/mochammadyusufpratama/makanku-asset/blob/main/bg_banner.jpg?raw=true")
        val isUsingGridMode = viewModel.isUsingGridMode()
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
        if (!viewModel.userIsLoggedIn()) {
            binding.layoutHeader.layoutHeader.tvName.apply {
                text = getString(R.string.text_display_name, "John Doe")
            }
        } else {
            val currentUser = viewModel.getCurrentUser()
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
            requireContext(), item
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
        viewModel.getCategories().observe(viewLifecycleOwner) {
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

                }
            )
        }
    }

    private fun getMenuData(categoryName: String? = null) {
        viewModel.getMenu(categoryName).observe(viewLifecycleOwner) {
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

                }
            )
        }
    }
    private fun bindModeList(isGridMode: Boolean) {
        val listMode =
            if (isGridMode)
                MenuAdapter.MODE_GRID
            else
                MenuAdapter.MODE_LIST
        menuAdapter = MenuAdapter(
            listMode = listMode,
            listener = object : MenuAdapter.OnItemClickedListener<Menu> {
                override fun onItemSelected(item: Menu) {
                    pushToDetail(item)
                }

                override fun onItemAddedToCart(item: Menu) {
                    viewModel.addItemToCart(item)
                }

            }
        )
        binding.rvProduct.apply {
            adapter = this@HomeFragment.menuAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                if (isGridMode)
                    2
                else
                    1
            )
        }

    }

    private fun setClickAction(usingGrid: Boolean) {
        var isGridMode = usingGrid
        binding.btnListToGrid.setOnClickListener {
            isGridMode = !isGridMode
            if (isGridMode)
                binding.btnListToGrid.setImageResource(R.drawable.ic_list)
            else
                binding.btnListToGrid.setImageResource(R.drawable.ic_grid)
            bindModeList(isGridMode)
            loadMenuData()
            viewModel.setUsingGridMode(isGridMode)
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