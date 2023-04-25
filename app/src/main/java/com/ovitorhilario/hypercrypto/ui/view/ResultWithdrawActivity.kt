package com.ovitorhilario.hypercrypto.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ovitorhilario.hypercrypto.R
import com.ovitorhilario.hypercrypto.databinding.ActivityResultWithdrawBinding

class ResultWithdrawActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultWithdrawBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent.hasExtra(KEY_CURRENT_WALLET_VALUE)) {

            val currencyWalletValue = intent.getDoubleExtra(KEY_CURRENT_WALLET_VALUE, 0.0)

            binding.tvWithdrawBalance.text = buildString {
                append(resources.getString(R.string.currency_balance))
                append(": $")
                append(currencyWalletValue)
            }

            binding.btnWithdrawAction.setOnClickListener {
                val valueToWithdraw = binding.inputWithdrawValue.text.toString()

                try {
                    valueToWithdraw.toDoubleOrNull()?.let { value ->
                        if (value > currencyWalletValue) {
                            Toast.makeText(
                                this@ResultWithdrawActivity,
                                resources.getString(R.string.withdraw_error_msg),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (value > 0 && value <= currencyWalletValue) {
                            Intent().apply {
                                putExtra(KEY_WITHDRAW_VALUE, value)
                                setResult(RESULT_OK, this)
                            }

                            finish()
                        }
                    }
                } catch (e: Exception) {
                    //
                    Intent().apply {
                        setResult(RESULT_CANCELED, this)
                    }

                    finish()
                }
            }
        }
    }

    companion object {
        const val KEY_CURRENT_WALLET_VALUE = "CURRENT_WALLET_VALUE"
        // const val KEY_DEPOSIT_VALUE = "DEPOSIT_VALUE"
        const val KEY_WITHDRAW_VALUE = "WITHDRAW_VALUE"
    }
}