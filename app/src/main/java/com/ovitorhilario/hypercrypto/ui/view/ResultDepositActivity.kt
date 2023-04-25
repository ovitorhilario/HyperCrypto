package com.ovitorhilario.hypercrypto.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ovitorhilario.hypercrypto.R
import com.ovitorhilario.hypercrypto.databinding.ActivityResultDepositBinding

class ResultDepositActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultDepositBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent.hasExtra(KEY_CURRENT_WALLET_VALUE)) {

            val currencyWalletValue = intent.getDoubleExtra(ResultWithdrawActivity.KEY_CURRENT_WALLET_VALUE, 0.0)

            binding.tvDepositBalance.text = buildString {
                append(resources.getString(R.string.currency_balance))
                append(": $")
                append(currencyWalletValue)
            }

            binding.btnDepositAction.setOnClickListener {
                val valueToDeposit = binding.inputDepositValue.text.toString()

                try {
                    valueToDeposit.toDoubleOrNull()?.let { value ->
                        if (value <= 0.0) {
                            Toast.makeText(
                                this@ResultDepositActivity,
                                resources.getString(R.string.deposit_error_msg),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (value > 0.0 && value <= 400000.0) {
                            Intent().apply {
                                putExtra(KEY_DEPOSIT_VALUE, value)
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
        const val KEY_DEPOSIT_VALUE = "DEPOSIT_VALUE"
        const val KEY_CURRENT_WALLET_VALUE = "CURRENT_WALLET_VALUE"
        // const val KEY_WITHDRAW_VALUE = "WITHDRAW_VALUE"
    }
}