package com.example.secondproject

import SampleListAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.secondproject.databinding.FragmentFourthBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FourthFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentFourthBinding
    private var adapter : SampleListAdapter? = null
    private val prettyGson: Gson = GsonBuilder().setPrettyPrinting().create()
    private var progressBar: ProgressBar? = null
    private var progressStatus = 0
    private val gadgetDatabase by lazy { GadgetDatabase.getDatabase(this).gadgetDao() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeGadget()

    }

    private fun initialise() {
        adapter = SampleListAdapter(this)
        binding.rvGadget.adapter = adapter

        callGadgetApi()
    }

    fun removeGadgetFromDb(item: Gadget) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gadgetDatabase.deleteGadgetById(item.id)
            }
        }
    }

    private fun callGadgetApi() {

        // _progressBar.value = Event(Event.SHOW_PROGRESS)
        progressStatus = 1

        val downloadService = provideRetrofit().create(ApiService::class.java)
        val call = downloadService.getApiResponse()
        call.enqueue( object : retrofit2.Callback<List<Gadget>> {
            override fun onResponse(call: retrofit2.Call<List<Gadget>>, response: retrofit2.Response<List<Gadget>>) {
                if (response.body() != null) {
                    val result = response.body()!!
                    if (response.isSuccessful) {
                        prettyGson.toJson(result)
                        Log.e("","ApiResponse--->${prettyGson.toJson(result)}")
                        // adapter!!.updateList(result.toMutableList()) IMPORTANT
                        // for loop
                        for ( gd in result) {
                            val newNote = Gadget(gd.id, gd.author, gd.width, gd.height, gd.url, gd.download_url)
                            lifecycleScope.launch {
                                gadgetDatabase.addGadget(newNote)
                            }
                        }
                    } else {
                        Log.e("","Server Contact failed")
                    }
                } else {
                    Log.e("","response.body is null")
                    //Toast.makeText(this@MainActivity,"response.body is null",Toast.LENGTH_SHORT).show()
                }
                //_progressBar.value = Event(Event.HIDE_PROGRESS)
            }
            override fun onFailure(call: retrofit2.Call<List<Gadget>>, t: Throwable) {
                //_progressBar.value = Event(Event.HIDE_PROGRESS)
                Log.e("","Api failed" + t.printStackTrace())
            }
        })
    }

    private fun observeGadget() {

        lifecycleScope.launch {
            Log.e("","Database--->${gadgetDatabase.getGadgets().toString()}")

            gadgetDatabase.getGadgets().collect { gadgetList ->
                if (gadgetList.isNotEmpty()) {
                    adapter!!.updateList(gadgetList.toMutableList())
                }
            }
        }
    }


    private fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor { message ->
            Log.e("","HttpLogging--> $message")
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
            .baseUrl("https://picsum.photos/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


//    private fun getInputList() : MutableList<Gadget> {
//        val inputList : MutableList<Gadget> = mutableListOf()
//        inputList.add(Gadget(1,"Jane Doe",R.drawable.logo))
//        inputList.add(Gadget(2,"Alex Liam",R.drawable.logo))
//        inputList.add(Gadget(3,"Keith Sequira",R.drawable.logo))
//        inputList.add(Gadget(4,"Peter Kate",R.drawable.logo))
//        inputList.add(Gadget(5,"Mery jane",R.drawable.logo))
//        inputList.add(Gadget(6,"Carlos Leo",R.drawable.logo))
//        inputList.add(Gadget(7,"Ben Parker",R.drawable.logo))
//        inputList.add(Gadget(8,"Anny Shane",R.drawable.logo))
//        inputList.add(Gadget(9,"Jerry Adney",R.drawable.logo))
//        inputList.add(Gadget(10,"Alejandro Vero",R.drawable.logo))
//        return inputList
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFourthBinding.inflate(layoutInflater)
        initialise()
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FourthFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FourthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
