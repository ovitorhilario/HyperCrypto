package com.ovitorhilario.hypercrypto.data.api

import com.ovitorhilario.hypercrypto.data.model.CryptoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoService {

    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getCryptoCurrencyLatest(
        @Query("CMC_PRO_API_KEY") API_KEY : String,
        @Query("convert") COIN_TYPE: String
    ) : CryptoResponse
}