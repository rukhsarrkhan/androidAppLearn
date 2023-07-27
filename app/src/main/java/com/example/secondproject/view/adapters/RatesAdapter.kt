package com.example.secondproject.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondproject.R
import com.example.secondproject.model.data.Rates
import com.example.secondproject.databinding.ItemRatesBinding
import com.example.secondproject.view.YouMatterFragment

class RatesAdapter(val context: YouMatterFragment) : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>(){

    private var ratesList: MutableList<Rates> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        return RatesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rates,parent,false))
    }

    override fun getItemCount(): Int {
        return ratesList.size
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val item = ratesList[position]
        holder.image.setImageResource(item.image)
        holder.name.text = item.name
        holder.value.text = item.value
    }

    fun updateList(inputList : MutableList<Rates>) {
        ratesList.clear()
        ratesList.addAll(inputList)
        notifyDataSetChanged()
    }

    inner class RatesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRatesBinding.bind(itemView)
        var layoutRates = binding.layoutRatesParameter
        val image = binding.imgParameter
        val name = binding.txtParameter
        val value = binding.txtParameterValue
    }

}