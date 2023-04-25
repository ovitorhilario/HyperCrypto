package com.ovitorhilario.hypercrypto.ui.model

import com.ovitorhilario.hypercrypto.data.model.Crypto

sealed class CryptoInfo {
    data class Tittle(val content: String) : CryptoInfo()
    data class Card(val balance: Double) : CryptoInfo()
    data class CryptoMetaData(val data: Crypto) : CryptoInfo()
}