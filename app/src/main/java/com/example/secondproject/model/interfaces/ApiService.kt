package com.example.secondproject.model.interfaces

import com.example.secondproject.model.data.Gadget
import com.example.secondproject.model.data.Session
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/list")
    fun getApiResponse(): Call<List<Gadget>>

    @GET("v2/list")
    fun getApiResponseWithLimit(@Query("page") page:Int, @Query("limit") limit:Int): Call<List<Gadget>>

    @GET("microservice_v3/event/v2/")
    fun getLiveSessionResponse(): Call<List<Session>>

}