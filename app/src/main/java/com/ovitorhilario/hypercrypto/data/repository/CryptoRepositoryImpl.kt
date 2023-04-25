package com.ovitorhilario.hypercrypto.data.repository

import com.ovitorhilario.hypercrypto.data.api.CryptoService
import com.ovitorhilario.hypercrypto.network.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class CryptoRepositoryImpl(
    private val service : CryptoService
) : CryptoRepository{

    override suspend fun getCryptoList(COIN_TYPE : String) =
        service.getCryptoCurrencyLatest(API_KEY, COIN_TYPE)

    companion object {
        const val API_KEY = "YOUR API KEY HERE @TODO"
    }
}