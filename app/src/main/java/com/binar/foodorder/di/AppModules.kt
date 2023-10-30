package com.binar.foodorder.di

import com.binar.foodorder.data.local.database.AppDatabase
import com.binar.foodorder.data.local.database.datasource.CartDataSource
import com.binar.foodorder.data.local.database.datasource.CartDatabaseDataSource
import com.binar.foodorder.data.local.datastore.UserPreferenceDataSource
import com.binar.foodorder.data.local.datastore.UserPreferenceDataSourceImpl
import com.binar.foodorder.data.local.datastore.ViewDataStoreManager
import com.binar.foodorder.data.local.datastore.appDataStore
import com.binar.foodorder.data.network.api.CategoryNetworkDataSource
import com.binar.foodorder.data.network.api.CategoryNetworkDataSourceImpl
import com.binar.foodorder.data.network.api.FoodNetworkDataSource
import com.binar.foodorder.data.network.api.FoodNetworkDataSourceImpl
import com.binar.foodorder.data.network.api.FoodService
import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSource
import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSourceImpl
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.data.repository.CartRepositoryImpl
import com.binar.foodorder.data.repository.FoodsRepository
import com.binar.foodorder.data.repository.FoodsRepositoryImpl
import com.binar.foodorder.data.repository.UserRepository
import com.binar.foodorder.data.repository.UserRepositoryImpl
import com.binar.foodorder.presentation.cart.CartViewModel
import com.binar.foodorder.presentation.detailfood.DetailViewModel
import com.binar.foodorder.presentation.homefood.FoodsViewModel
import com.binar.foodorder.presentation.login.LoginViewModel
import com.binar.foodorder.presentation.main.MainViewModel
import com.binar.foodorder.presentation.profil.ProfileViewModel
import com.binar.foodorder.presentation.register.RegisterViewModel
import com.binar.foodorder.presentation.splash.SplashViewModel
import com.binar.foodorder.util.PreferenceDataStoreHelper
import com.binar.foodorder.util.PreferenceDataStoreHelperImpl
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
        single { androidContext().appDataStore }
        single<PreferenceDataStoreHelper> { PreferenceDataStoreHelperImpl(get()) }
    }
    private val networKmodule = module {
        single { FirebaseAuth.getInstance() }
        single { FoodService.invoke() }
    }
    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<UserPreferenceDataSource> { UserPreferenceDataSourceImpl(get()) }
        single<FoodNetworkDataSource> { FoodNetworkDataSourceImpl(get()) }
        single<CategoryNetworkDataSource> { CategoryNetworkDataSourceImpl(get()) }
        single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
        single { ViewDataStoreManager(androidContext()) }
    }
    private val RepositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get(), get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
        single<FoodsRepository> { FoodsRepositoryImpl(get(), get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::FoodsViewModel)
        viewModelOf(::MainViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::SplashViewModel)
        viewModel { params -> DetailViewModel(params.get(), get()) }
        viewModelOf(::CartViewModel)
        viewModelOf(::ProfileViewModel)
    }
    val modules: List<Module> = listOf(
        localModule,
        networKmodule,
        dataSourceModule,
        RepositoryModule,
        viewModelModule
    )
}
