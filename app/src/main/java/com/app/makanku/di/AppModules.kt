package com.app.makanku.di

import android.content.SharedPreferences
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
import com.app.makanku.data.repository.CartRepository
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
import com.app.makanku.data.source.local.database.dao.CartDao
import com.app.makanku.data.source.local.pref.UserPreference
import com.app.makanku.data.source.local.pref.UserPreferenceImpl
import com.app.makanku.data.source.network.services.FoodAppApiService
import com.app.makanku.presentation.cart.CartViewModel
import com.app.makanku.presentation.checkout.CheckoutViewModel
import com.app.makanku.presentation.detailproduct.DetailProductViewModel
import com.app.makanku.presentation.home.HomeViewModel
import com.app.makanku.presentation.login.LoginViewModel
import com.app.makanku.presentation.main.MainViewModel
import com.app.makanku.presentation.profile.ProfileViewModel
import com.app.makanku.presentation.register.RegisterViewModel
import com.app.makanku.presentation.splashscreen.SplashViewModel
import com.app.makanku.utils.SharedPreferenceUtils
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object AppModules {
    val networkModule =
        module {
            single<FoodAppApiService> { FoodAppApiService.invoke() }
        }

    private val firebaseModule =
        module {
            single<FirebaseAuth> { FirebaseAuth.getInstance() }
            single<FirebaseService> { FirebaseServiceImpl(get()) }
        }

    val localModule =
        module {
            single<AppDatabase> { AppDatabase.createInstance(androidContext()) }
            single<CartDao> { get<AppDatabase>().cartDao() }
            single<SharedPreferences> {
                SharedPreferenceUtils.createPreference(
                    androidContext(),
                    UserPreferenceImpl.PREF_NAME,
                )
            }
            single<UserPreference> { UserPreferenceImpl(get()) }
        }

    private val datasource =
        module {
            single<AuthDataSource> { FirebaseAuthDataSource(get()) }
            single<CartDataSource> { CartDatabaseDataSource(get()) }
            single<CategoryDataSource> { CategoryApiDataSource(get()) }
            single<MenuDataSource> { MenuApiDataSource(get()) }
            single<UserPrefDataSource> { UserPrefDataSourceImpl(get()) }
        }

    private val repository =
        module {
            single<CartRepository> { CartRepositoryImpl(get()) }
            single<CategoryRepository> { CategoryRepositoryImpl(get()) }
            single<MenuRepository> { MenuRepositoryImpl(get(), get()) }
            single<UserPrefRepository> { UserPrefRepositoryImpl(get()) }
            single<UserRepository> { UserRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::CartViewModel)
            viewModelOf(::CheckoutViewModel)
            viewModel { params ->
                DetailProductViewModel(
                    extras = params.get(),
                    cartRepository = get(),
                )
            }
            viewModelOf(::HomeViewModel)
            viewModelOf(::LoginViewModel)
            viewModelOf(::MainViewModel)
            viewModelOf(::RegisterViewModel)
            viewModelOf(::SplashViewModel)
            viewModelOf(::ProfileViewModel)
        }

    val modules =
        listOf(
            networkModule,
            firebaseModule,
            localModule,
            datasource,
            repository,
            viewModelModule,
        )
}
