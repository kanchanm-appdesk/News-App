package com.appdesk.newsapp.data.remote

import com.appdesk.newsapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    private val createInstance by lazy {
        //logs HTTP request and response data
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        //build API through Retrofit builder class
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    //to use lazy for initialized object once when it is called and use the same object again
    val getInstance: NewsApi by lazy {
        createInstance.create(NewsApi::class.java)
    }
}