package com.ovitorhilario.hypercrypto.di.module

import com.ovitorhilario.hypercrypto.data.api.CryptoService
import com.ovitorhilario.hypercrypto.data.repository.CryptoRepository
import com.ovitorhilario.hypercrypto.data.repository.CryptoRepositoryImpl
import com.ovitorhilario.hypercrypto.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CryptoService::class.java)
    }

    single<CryptoRepository> {
        CryptoRepositoryImpl(get())
    }

    viewModel {
        MainViewModel(get())
    }
}