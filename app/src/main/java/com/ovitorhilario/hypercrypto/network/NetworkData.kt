package com.ovitorhilario.hypercrypto.network

import com.ovitorhilario.hypercrypto.data.model.Crypto

sealed class Response {
    data class Success(val data: List<Crypto>) : Response()
    data class Error(val message: String?) : Response()
}