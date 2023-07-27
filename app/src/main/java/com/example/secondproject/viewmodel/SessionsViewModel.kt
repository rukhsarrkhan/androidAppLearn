package com.example.secondproject.viewmodel

import android.util.Log
import com.example.secondproject.model.data.Session
import com.example.secondproject.model.interfaces.ApiService
import com.example.secondproject.view.adapters.SessionAdpater
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SessionsViewModel {
    private val prettyGson: Gson = GsonBuilder().setPrettyPrinting().create()
    private var sessionAdpater: SessionAdpater? = null

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

}