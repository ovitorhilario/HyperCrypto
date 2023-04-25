package com.ovitorhilario.hypercrypto.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ovitorhilario.hypercrypto.R
import com.ovitorhilario.hypercrypto.databinding.FragmentHomeBinding
import com.ovitorhilario.hypercrypto.ui.adatper.HomeAdapter
import com.ovitorhilario.hypercrypto.ui.dataStore
import com.ovitorhilario.hypercrypto.ui.model.CryptoInfo
import com.ovitorhilario.hypercrypto.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModel()

    private val registerForDeposit = registerForActivityResult (
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

            intent?.getDoubleExtra(KEY_DEPOSIT_VALUE, 0.0)?.let {
                viewModel.doDeposit(it)
            }
        }
    }

    private val registerForWithdraw = registerForActivityResult (
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

            intent?.getDoubleExtra(KEY_WITHDRAW_VALUE, 0.0)?.let {
                viewModel.doWithdraw(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, group, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.currentCrypto.observe(viewLifecycleOwner) { list ->
            try {
                list?.let { cryptos ->
                    val listAdapter = mutableListOf(
                        CryptoInfo.Tittle("My Wallet"),
                        CryptoInfo.Card(viewModel.walletPrice.value ?: 0.0)
                    )

                    cryptos.forEach { crypto ->
                        listAdapter.add(CryptoInfo.CryptoMetaData(crypto))
                    }

                    binding.rvHome.adapter = HomeAdapter(
                        data = listAdapter,
                        depositAction = { openDeposit() },
                        withDrawAction = { openWithdraw() },
                        settingsAction = { openSettings() },
                        COIN_TYPE = runBlocking { getCurrentCoin() }
                    )
                }
            } catch (e: Exception) {
                Log.e("ERROR TO SET RECYCLERVIEW :", e.message.toString());
            }
        }
    }

    private fun openSettings() {
        val singleItems = arrayOf("BRL", "USD", "EUR")
        var checkedItem = 0

        lifecycleScope.launch {
            checkedItem = singleItems.indexOf(getCurrentCoin())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.what_is_the_currency_type))
            .setNeutralButton("Cancel") { _, _ -> }
            .setPositiveButton("Save") { dialog, which ->
                lifecycleScope.launch { setCurrencyCoin(singleItems[checkedItem]) }
            }
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                checkedItem = which
            }
            .show()
    }

    private fun openDeposit() {
        registerForDeposit.launch(Intent(requireContext(), ResultDepositActivity::class.java).apply {
            this.putExtra(KEY_CURRENT_WALLET_VALUE, viewModel.walletPrice.value)
        })
    }

    private fun openWithdraw() {
        registerForWithdraw.launch(Intent(requireContext(), ResultWithdrawActivity::class.java).apply {
            this.putExtra(KEY_CURRENT_WALLET_VALUE, viewModel.walletPrice.value)
        })
    }

    private suspend fun setCurrencyCoin(type: String) {
        this@HomeFragment.requireContext().dataStore.edit { settings ->
            settings[COIN_TYPE] = type
            if (viewModel.coinType.value != type) {
                viewModel.coinType.value = type
                viewModel.fetchData()
            }
        }
    }

    private suspend fun getCurrentCoin() : String {
        val coinType = this@HomeFragment.requireContext().dataStore.data.first()
        return coinType[COIN_TYPE] ?: "BRL"
    }

    companion object {
        val COIN_TYPE = stringPreferencesKey("coin_type")
        const val KEY_CURRENT_WALLET_VALUE = "CURRENT_WALLET_VALUE"
        const val KEY_DEPOSIT_VALUE = "DEPOSIT_VALUE"
        const val KEY_WITHDRAW_VALUE = "WITHDRAW_VALUE"
    }
}