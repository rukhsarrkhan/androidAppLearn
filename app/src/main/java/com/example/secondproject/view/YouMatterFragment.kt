package com.example.secondproject.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondproject.model.interfaces.ApiService
import com.example.secondproject.model.data.Exercise
import com.example.secondproject.view.adapters.ExerciseAdapter
import com.example.secondproject.R
import com.example.secondproject.model.data.Rates
import com.example.secondproject.view.adapters.RatesAdapter
import com.example.secondproject.model.data.Session
import com.example.secondproject.view.adapters.SessionAdpater
import com.example.secondproject.databinding.FragmentYouMatterBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class YouMatterFragment : Fragment(), SessionAdpater.SessionAdapterListener {
    private lateinit var binding : FragmentYouMatterBinding
    private var exerciseAdapter: ExerciseAdapter? = null
    private var sessionAdpater: SessionAdpater? = null
    private var ratesAdpater: RatesAdapter? = null
    private val prettyGson: Gson = GsonBuilder().setPrettyPrinting().create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYouMatterBinding.inflate(layoutInflater, container, false)
        try {
            initialise()
            binding.scanButton!!.setOnClickListener{
                binding.scanlayout!!.visibility = View.GONE
                binding.scanlayoutData!!.visibility = View.VISIBLE
            }
            binding.rescanButton!!.setOnClickListener{
                binding.scanlayoutData!!.visibility = View.GONE
                binding.scanlayout!!.visibility = View.VISIBLE
            }

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return binding.root;
    }

    private fun initialise() {
        exerciseAdapter = ExerciseAdapter(this)
//        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvExercises.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvExercises.adapter = exerciseAdapter
        exerciseAdapter!!.updateList(getExercises())

        sessionAdpater = SessionAdpater(this, this)
        binding.rvSessions.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSessions.adapter = sessionAdpater
        sessionAdpater?.notifyDataSetChanged()
        callGadgetApi()

        ratesAdpater = RatesAdapter(this)
        binding.rvRates.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvRates.adapter = ratesAdpater
        ratesAdpater!!.updateList(getRates())
    }

     override fun joinNowClick(sessionModal : Session) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(sessionModal.link));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.android.chrome")
            context?.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun callGadgetApi() {
        val apiService = provideRetrofit().create(ApiService::class.java)
        val call = apiService.getLiveSessionResponse()

        call.enqueue( object : retrofit2.Callback<List<Session>> {
            override fun onResponse(call: retrofit2.Call<List<Session>>, response: retrofit2.Response<List<Session>>) {
                Log.e("page", "ApiResponse--->${response.body()}")

                if (response.body() != null) {
                    val result = response.body()!!
                    if (response.isSuccessful) {
                        prettyGson.toJson(result)
                        Log.e("page", "ApiResponse--->${prettyGson.toJson(result)}")
                        sessionAdpater!!.updateList(result.toMutableList()) // IMPORTANT
                    } else {
                        Log.e("page", "Server Contact failed")
                    }
                } else {
                    Log.e("", "response.body is null")
                }
            }
            override fun onFailure(call: retrofit2.Call<List<Session>>, t: Throwable) {
                Log.e("", "Api failed" + t.printStackTrace())
            }
        })
    }

    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor { message ->
            Log.e("", "HttpLogging--> $message")
        }
        logging.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .protocols(listOf(Protocol.HTTP_1_1))
                    .addInterceptor(logging)
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .build()
            )
            .baseUrl("https://apiv2.smit.fit/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getExercises(): MutableList<Exercise> {
        val list: MutableList<Exercise> = mutableListOf()
        list.add(Exercise(1, "Meditation", R.drawable.meditation, "#F16495"))
        list.add(Exercise(2, "Yoga", R.drawable.yoga, "#80A684"))
        list.add(Exercise(3, "Exercise", R.drawable.exercise, "#E89A91"))
        return list
    }

    fun getSessions(): MutableList<Session> {
        val list: MutableList<Session> = mutableListOf()
        list.add(
            Session(
                "1",
                "Yoga Session",
                "7:00:00",
                "On Going",
                "https://us02web.zoom.us/j/84627074500?pwd=RDVXQ1V4Z2JGR3ZqK3BxQ1c4VzJSdz09",
                R.drawable.yoga_session,
                1
            )
        )
        list.add(
            Session(
                "2",
                "Meditation Session",
                "7:00:00",
                "25th July, 3 PM",
                "https://us02web.zoom.us/j/84627074500?pwd=RDVXQ1V4Z2JGR3ZqK3BxQ1c4VzJSdz09",
                R.drawable.meditation_session,
                2
            )
        )
        return list
    }

    fun getRates(): MutableList<Rates> {
        val list: MutableList<Rates> = mutableListOf()
        list.add(Rates(1, "Heart rate", "99bpm", R.drawable.heart_rate))
        list.add(Rates(2, "Breathing rate", "99bpm", R.drawable.heart_rate))
        list.add(Rates(3, "BMI", "21.6", R.drawable.bmi_rate))
        list.add(Rates(4, "Blood pressure", "99bpm", R.drawable.blood_pressure_rate))
        return list
    }

    companion object {
    }
}