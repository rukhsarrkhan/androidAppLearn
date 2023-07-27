package com.example.secondproject.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondproject.view.FourthFragment
import com.example.secondproject.model.data.Gadget
import com.example.secondproject.R
import com.example.secondproject.databinding.ListViewItemsBinding
import com.squareup.picasso.Picasso

class SampleListAdapter(val context: FourthFragment) : RecyclerView.Adapter<SampleListAdapter.SampleListViewHolder>() {

    private var gadgetList: MutableList<Gadget> = mutableListOf()

    override fun getItemCount(): Int {
        return gadgetList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleListViewHolder {
        return SampleListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_view_items,parent,false))
    }

    override fun onBindViewHolder(holder: SampleListViewHolder, position: Int) {
        val item = gadgetList[position]
        holder.txtTitle.text = item.id
        holder.txtDesc.text = item.author
        holder.txtUrl.text = item.download_url
        Picasso.get()
            .load(item.download_url)
            .placeholder(R.drawable.img_placeholder)
            .into(holder.imgGadget)

        holder.delete!!.setOnClickListener {
            onItemRemoveClick(position, item)
        }
    }

    fun updateList(inputList : MutableList<Gadget>) {
        // gadgetList.clear()
        gadgetList.addAll(inputList)
        notifyDataSetChanged()
    }

    inner class SampleListViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListViewItemsBinding.bind(itemView)
        val imgGadget = binding.imgAvtar
        val txtTitle = binding.txtTitle
        val txtDesc = binding.txtDesc
        val txtUrl = binding.txtUrl
        val delete = binding.imageButton
    }

    fun onItemRemoveClick(position: Int, item: Gadget) {
        context.removeGadgetFromDb(item)
        gadgetList.removeAt(position)
    }

}