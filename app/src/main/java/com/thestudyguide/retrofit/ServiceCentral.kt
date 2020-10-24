package com.thestudyguide.retrofit

import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServiceCentral {
    private val BASE_URL = "BASE URL HERE" //Todo: Add Base Url
    private val api: APICentral

    init {
        val clientOkHttpClient = OkHttpClient().newBuilder().build()
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(clientOkHttpClient)
            .build()
            .create(APICentral::class.java)
    }

    //TODO Change model T
    fun<T> builder(): Observable<T> {
        return api.getSomething()
    }

}