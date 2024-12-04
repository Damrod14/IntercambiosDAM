package com.example.appintercambios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExchangeAdapter(private val exchanges: List<Exchange>) :
    RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    class ExchangeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvExchangeName: TextView = view.findViewById(R.id.tvExchangeName)
        val tvExchangeDate: TextView = view.findViewById(R.id.tvExchangeDate)
        val tvParticipants: TextView = view.findViewById(R.id.tvParticipants)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exchange, parent, false)
        return ExchangeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        val exchange = exchanges[position]
        holder.tvExchangeName.text = exchange.name
        holder.tvExchangeDate.text = "Fecha: ${exchange.date}"
        holder.tvParticipants.text = "Participantes: ${exchange.participants}"
    }

    override fun getItemCount(): Int = exchanges.size
}
