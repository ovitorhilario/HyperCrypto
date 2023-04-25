package com.ovitorhilario.hypercrypto.data.repository

import com.ovitorhilario.hypercrypto.data.model.CryptoResponse

interface CryptoRepository {
    suspend fun getCryptoList(COIN_TYPE : String = "BRL") : CryptoResponse
}