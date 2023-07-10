package com.example.secondproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondproject.databinding.ItemSmitFitBinding

class ExerciseAdapter(val context: YouMatterFragment) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>(){

    private var exerciseList: MutableList<Exercise> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_smit_fit,parent,false))
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val item = exerciseList[position]
        holder.imgFeature.setImageResource(item.image)
        holder.txtFeature.text = item.name

        holder.layoutSmitFit.setOnClickListener {
//            listener.onSmitFitFeatureClick(feature)
        }
    }

    fun updateList(inputList : MutableList<Exercise>) {
        exerciseList.clear()
        exerciseList.addAll(inputList)
        notifyDataSetChanged()
    }

    inner class ExerciseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSmitFitBinding.bind(itemView)
        var layoutSmitFit = binding.layoutSmitFit
        val txtFeature = binding.txtFeature
        val imgFeature = binding.imgFeature
    }
}