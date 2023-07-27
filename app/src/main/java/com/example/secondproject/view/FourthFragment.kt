package com.example.secondproject.view

import com.example.secondproject.view.adapters.SampleListAdapter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondproject.model.interfaces.ApiService
import com.example.secondproject.model.data.Gadget
import com.example.secondproject.model.GadgetDatabase
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

@Suppress("DEPRECATION")
class FourthFragment : Fragment() {

    private lateinit var binding : FragmentFourthBinding
    private val prettyGson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val gadgetDatabase by lazy { GadgetDatabase.getDatabase(this).gadgetDao() }
    private var progressBar: ProgressBar? = null
    var recyclerView: RecyclerView? = null

    var page = 1
    var isLoading = false
    val limit = 10

    lateinit var adapter : SampleListAdapter
    lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = binding.rvGadget.layoutManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFourthBinding.inflate(layoutInflater)
        recyclerView = binding.rvGadget;

        getPage();

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.e("page", "dx--->${dx}")
                Log.e("page", "dy--->${dy}")
//                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                Log.e("page", "visibleItemCount--->${visibleItemCount}")
                val pastVisibleItem = layoutManager?.findFirstCompletelyVisibleItemPosition()
                Log.e("page", "pastVisibleItem--->${pastVisibleItem}")
                val total = adapter?.itemCount
                Log.e("page", "total--->${total}")
                Log.e("page", "isLoading--->${isLoading}")
                if(!isLoading) {
//                            if ((visibleItemCount + pastVisibleItem!!) >= total!!) {
                                page++
                                getPage()
//                            }
                    }
                //}
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        // observeGadget()
        return binding.root
    }


    private fun getPage() {
        isLoading = true;
        progressBar?.visibility = View.VISIBLE
        val start = (page-1) * limit
        val end = page * limit

        Handler().postDelayed({
            if (::adapter.isInitialized) {
                Log.e("page", "1--->${page}")

                adapter?.notifyDataSetChanged()
                callGadgetApi(page)

            } else {
                Log.e("page", "2--->${page}")

                adapter = SampleListAdapter(this)
                binding.rvGadget.adapter = adapter
                callGadgetApi(page)


            }

            isLoading = false;
            progressBar?.visibility = View.GONE
        }, 5000)

    }

    private fun observeGadget() {
        // isLoading = true;
        // progressBar?.visibility = View.VISIBLE
        lifecycleScope.launch {
            Log.e("", "Database--->${gadgetDatabase.getGadgets().toString()}")

            gadgetDatabase.getGadgets().collect { gadgetList ->

                if (gadgetList.isNotEmpty()) {
                    // isLoading = false;
                    // progressBar?.visibility = View.GONE
                    adapter!!.updateList(gadgetList.toMutableList())
                }
            }
        }
    }

    fun removeGadgetFromDb(item: Gadget) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gadgetDatabase.deleteGadgetById(item.id)
            }
        }
    }

    private fun callGadgetApi(page: Int) {
        Log.e("page", "page--->${page}")
//        isLoading = true;
//        progressBar?.visibility = View.VISIBLE
        val downloadService = provideRetrofit().create(ApiService::class.java)
        val call = downloadService.getApiResponseWithLimit(page, limit)

        call.enqueue( object : retrofit2.Callback<List<Gadget>> {
            override fun onResponse(call: retrofit2.Call<List<Gadget>>, response: retrofit2.Response<List<Gadget>>) {
                Log.e("page", "ApiResponse--->${response.body()}")

                if (response.body() != null) {

                    val result = response.body()!!
                    if (response.isSuccessful) {
                        prettyGson.toJson(result)
                        Log.e("", "ApiResponse--->${prettyGson.toJson(result)}")
                        adapter!!.updateList(result.toMutableList()) // IMPORTANT
                        // for loop
//                        for ( gd in result) {
//                            val newNote = Gadget(gd.id, gd.author, gd.width, gd.height, gd.url, gd.download_url)
//                            lifecycleScope.launch {
//                                gadgetDatabase.addGadget(newNote)
//                            }
//                        }

                         isLoading = false;
                         progressBar?.visibility = View.GONE
                    } else {
                        Log.e("", "Server Contact failed")
                    }
                } else {
                    Log.e("", "response.body is null")
                }
                isLoading = false;
                progressBar?.visibility = View.GONE
            }
            override fun onFailure(call: retrofit2.Call<List<Gadget>>, t: Throwable) {
                Log.e("", "Api failed" + t.printStackTrace())
            }
        })
    }

    private fun provideRetrofit(): Retrofit {
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
            .baseUrl("https://picsum.photos/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FourthFragment().apply {
            }
    }
}