package com.ovitorhilario.hypercrypto.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovitorhilario.hypercrypto.data.model.Crypto
import com.ovitorhilario.hypercrypto.data.repository.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: CryptoRepository
) : ViewModel() {

    private val _currentCrypto = MutableLiveData<List<Crypto>>()
    val currentCrypto get() = _currentCrypto as LiveData<List<Crypto>>

    private val _walletPrice = MutableLiveData<Double>(1235.54)
    val walletPrice get() = _walletPrice as LiveData<Double>

    val coinType = MutableLiveData<String>();

    fun doDeposit(newPrice: Double)  = _walletPrice.setValue((_walletPrice.value ?: 0.0) + newPrice)
    fun doWithdraw(withDrawPrice: Double) = _walletPrice.setValue(_walletPrice.value?.minus(withDrawPrice) ?: 0.0)

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.getCryptoList(coinType.value ?: "BRL")

            withContext(Dispatchers.Main) {
                res?.data?.let { _currentCrypto.value = it }
            }
        }
    }
}