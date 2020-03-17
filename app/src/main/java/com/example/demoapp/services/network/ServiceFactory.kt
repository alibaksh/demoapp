package com.example.demoapp.services.network

import com.example.demoapp.constants.EnvironmentConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


/**
 * Created on 2020-01-18.
 */
object ServiceFactory {

    //Creating Auth Interceptor to add api_key query in front of all the requests.
    private val authInterceptor = Interceptor { chain ->

        val request = chain.request()
        val url = request.url()
            .newBuilder()
            .addQueryParameter("api-key","Z8caG52CxFGcldSDVMRkQedoUcBafqie")
            .build()

        val newRequest = request.newBuilder()
            .url(url).build()

        chain.proceed(newRequest)
    }

    //OkhttpClient for building http request url
    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .build()

    var gson = GsonBuilder()
        .setLenient()
        .create()

    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(EnvironmentConstants.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    var nyService: NYService = retrofit().create(NYService::class.java)

}