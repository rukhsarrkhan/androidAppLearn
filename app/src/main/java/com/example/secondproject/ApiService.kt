package com.example.secondproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/list")
    fun getApiResponse(): Call<List<Gadget>>

    @GET("v2/list")
    fun getApiResponseWithLimit(@Query("page") page:Int, @Query("limit") limit:Int): Call<List<Gadget>>

}