package com.thestudyguide.retrofit

import io.reactivex.Observable
import retrofit2.http.GET

interface APICentral {

    @GET("") //TODO: Add value
    fun getSomething(): Observable<T>  //TODO: Change function name , change model T
}