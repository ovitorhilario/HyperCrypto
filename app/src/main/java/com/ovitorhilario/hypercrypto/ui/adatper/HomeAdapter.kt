package com.ovitorhilario.hypercrypto.ui.adatper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ovitorhilario.hypercrypto.R
import com.ovitorhilario.hypercrypto.databinding.CardHomeItemBinding
import com.ovitorhilario.hypercrypto.databinding.CryptoHomeItemBinding
import com.ovitorhilario.hypercrypto.databinding.TittleHomeItemBinding
import com.ovitorhilario.hypercrypto.ui.model.CryptoInfo
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class HomeAdapter(
    private val data: List<CryptoInfo>,
    private val depositAction : () -> Unit,
    private val withDrawAction: () -> Unit,
    private val settingsAction: () -> Unit,
    private val COIN_TYPE: String
) : Adapter<ViewHolder>() {

    class TittleItem(
        binding: TittleHomeItemBinding,
        val settingsAction : () -> Unit
    ) : ViewHolder(binding.root) {
        private val tvTittleInfo = binding.tvTittleInfo
        private val btnTittleSettings = binding.btnTittleSettings

        fun bind1(tittle: CryptoInfo.Tittle) {
            tvTittleInfo.text = tittle.content
            btnTittleSettings.setOnClickListener { settingsAction() }
        }
    }

    class CryptoItem(binding: CryptoHomeItemBinding) : ViewHolder(binding.root) {
        private val tvCryptoName = binding.tvCryptoName
        private val tvCryptoPercentState = binding.tvCryptoPercentState
        private val tvCryptoPrice = binding.tvCryptoPrice
        private val ivCryptoIcon = binding.ivCryptoIcon
        private val ivCryptoArrowTrend = binding.ivCryptoArrowTrend
        private val dec = DecimalFormat("#,###.##")

        fun bind2(crypto: CryptoInfo.CryptoMetaData, COIN_TYPE: String) {
            tvCryptoName.text = crypto.data.symbol
            var price: Double = 0.0
            var percent: Double = 0.0
            var symbol : String = ""

            try {
                when (COIN_TYPE) {
                    "BRL" -> {
                        price = crypto.data.quote.BRL.price
                        percent = crypto.data.quote.BRL.percent_change_24h
                        symbol = "R$"
                    }
                    "USD" -> {
                        price = crypto.data.quote.USD.price
                        percent = crypto.data.quote.USD.percent_change_24h
                        symbol = "$"
                    }
                    "EUR" -> {
                        price = crypto.data.quote.EUR.price
                        percent = crypto.data.quote.EUR.percent_change_24h
                        symbol = "€"
                    }
                    else -> {
                        price = crypto.data.quote.BRL.price
                        percent = crypto.data.quote.BRL.percent_change_24h
                    }
                }
            } catch (e: NullPointerException) {
                //
            }

            tvCryptoPrice.text = buildString {
                append(symbol)
                append(dec.format(price))
            }

            tvCryptoPercentState.text = buildString {
                if(percent > 0) {
                    append("+")
                    ivCryptoArrowTrend.setImageResource(R.drawable.ic_arrow_up)
                } else if(percent < 0) {
                    ivCryptoArrowTrend.setImageResource(R.drawable.ic_arrow_down)
                }

                append(String.format("%.2f", percent))
                append("%")
            }

            val pathIconUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/${crypto.data.id}.png"

            Picasso.get()
                .load(pathIconUrl)
                .into(ivCryptoIcon)
        }
    }


    class CardItem(
        binding: CardHomeItemBinding,
        val AcDeposit : () -> Unit,
        val AcWithDraw: () -> Unit
    ) : ViewHolder(binding.root)
    {
        private val tvCardBalance = binding.tvCardBalance
        private val btnCardDeposit = binding.btnCardDeposit
        private val btnCardWithDraw = binding.btnCardWithdraw
        private val dec = DecimalFormat("#,###.##")

        fun bind3(card: CryptoInfo.Card) {
            tvCardBalance.text = buildString {
                append("$")
                append(dec.format(card.balance))
            }

            btnCardDeposit.setOnClickListener { AcDeposit() }
            btnCardWithDraw.setOnClickListener { AcWithDraw() }
        }
    }

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(group.context)

        return when (viewType) {
            CryptoType.TITTLE.id -> TittleItem(
                TittleHomeItemBinding.inflate(inflater, group, false), settingsAction
            )
            CryptoType.CRYPTOS.id -> CryptoItem(
                CryptoHomeItemBinding.inflate(inflater, group, false)
            )
            CryptoType.CARD.id -> CardItem(
                CardHomeItemBinding.inflate(inflater, group, false), depositAction, withDrawAction
            )
            else -> throw IllegalArgumentException("invalid view type holder.")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is TittleItem -> {
                holder.bind1(data[position] as CryptoInfo.Tittle)
            }
            is CryptoItem -> {
                holder.bind2(data[position] as CryptoInfo.CryptoMetaData, COIN_TYPE)
            }
            is CardItem -> {
                holder.bind3(data[position] as CryptoInfo.Card)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is CryptoInfo.Tittle -> CryptoType.TITTLE.id
            is CryptoInfo.CryptoMetaData -> CryptoType.CRYPTOS.id
            is CryptoInfo.Card -> CryptoType.CARD.id
        }
    }

    override fun getItemCount(): Int = data.size

    enum class CryptoType(val id: Int) {
        TITTLE(1),
        CRYPTOS(2),
        CARD(3),
    }
}