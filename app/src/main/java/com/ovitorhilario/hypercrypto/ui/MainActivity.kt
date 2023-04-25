package com.ovitorhilario.hypercrypto.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.ovitorhilario.hypercrypto.R
import com.ovitorhilario.hypercrypto.databinding.ActivityMainBinding
import com.ovitorhilario.hypercrypto.ui.view.HomeFragment
import com.ovitorhilario.hypercrypto.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mViewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        changeForThis<HomeFragment>()
        val coinType = runBlocking { getCurrentCoin() }
        mViewModel.coinType.value = coinType
    }

    override fun onStart() {
        super.onStart()

        mViewModel.fetchData()
    }

    private suspend fun getCurrentCoin() : String {
        val coinType = dataStore.data.first()
        return coinType[COIN_TYPE] ?: "BRL"
    }

    private inline fun <reified T : Fragment> changeForThis() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<T>(R.id.fcv_main)
        }
    }

    companion object {
        val COIN_TYPE = stringPreferencesKey("coin_type")
    }
}