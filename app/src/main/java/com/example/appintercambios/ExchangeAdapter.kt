package com.example.appintercambios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExchangeAdapter(private val exchangeList: List<String>) :
    RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    class ExchangeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ExchangeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        holder.textView.text = exchangeList[position]
    }

    override fun getItemCount(): Int {
        return exchangeList.size
    }
}
