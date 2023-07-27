package com.example.secondproject.view.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondproject.model.data.Exercise
import com.example.secondproject.R
import com.example.secondproject.databinding.ItemExerciseBinding
import com.example.secondproject.view.YouMatterFragment

class ExerciseAdapter(val context: YouMatterFragment) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>(){

    private var exerciseList: MutableList<Exercise> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_exercise,parent,false))
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val item = exerciseList[position]
        holder.imgFeature.setImageResource(item.image)
        holder.txtFeature.text = item.name
        holder.txtFeature.setTextColor(Color.parseColor(item.color))
    }

    fun updateList(inputList : MutableList<Exercise>) {
        exerciseList.clear()
        exerciseList.addAll(inputList)
        notifyDataSetChanged()
    }

    inner class ExerciseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemExerciseBinding.bind(itemView)
        var layoutSmitFit = binding.layoutSmitFit
        val txtFeature = binding.txtFeature
        val imgFeature = binding.imgFeature
    }
}