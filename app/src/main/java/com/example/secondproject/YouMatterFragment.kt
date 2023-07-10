package com.example.secondproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondproject.databinding.FragmentYouMatterBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.Locale

class YouMatterFragment : Fragment()  {

    private lateinit var binding : FragmentYouMatterBinding
    // private var recyclerView: RecyclerView? = null
    private var exerciseAdapter: ExerciseAdapter? = null

//    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        layoutManager = LinearLayoutManager(context)
        // recyclerView?.layoutManager = binding.rvSmitFit.layoutManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYouMatterBinding.inflate(layoutInflater, container, false)
        // recyclerView = binding.rvSmitFit;

        try {
            initialise()
//            setClickable()
//            setObserver()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_you_matter, container, false)
    }

    private fun initialise() {

        // startShimmer()
        configureRecyclerView()

        exerciseAdapter = ExerciseAdapter(this)
        binding.rvSmitFit.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvSmitFit.adapter = exerciseAdapter
        exerciseAdapter!!.updateList(getSmitFitFeatures())
    }

//    private fun startShimmer() {
//        binding.layoutShimmer.startShimmer()
//        binding.layoutShimmer.visibility = View.VISIBLE
//    }

    private fun configureRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        binding.rvBlogs.layoutManager = layoutManager
//        blogAdapter = BlogDashboardAdapter(this,viewModel,this)
//        binding.rvBlogs.adapter = blogAdapter
    }

    fun getSmitFitFeatures(): MutableList<Exercise> {
        val list: MutableList<Exercise> = mutableListOf()
        list.add(Exercise(1,"Meditation",R.drawable.meditation))
        list.add(Exercise(1,"Yoga",R.drawable.yoga))
        list.add(Exercise(3,"Exercise",R.drawable.exercise))
        return list
    }

    companion object {
    }
}
