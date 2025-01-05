package com.example.appintercambios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Exchange(
    val name: String,
    val date: String,
    val location: String,
    val maxAmount: Int,
    val theme: String,
    val comments: String,
    val emails: List<String>,
    val uniqueKey: String
)

class ExchangeAdapter(private val exchanges: List<Exchange>) : RecyclerView.Adapter<ExchangeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvExchangeName)
        val dateTextView: TextView = view.findViewById(R.id.tvExchangeDate)
        val maxAmountTextView: TextView = view.findViewById(R.id.tvExchangemaxAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exchange, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exchange = exchanges[position]
        holder.nameTextView.text = exchange.name
        holder.dateTextView.text = exchange.date
        holder.maxAmountTextView.text = "Costo Máx: ${exchange.maxAmount}"
    }

    override fun getItemCount(): Int {
        return exchanges.size
    }

}

