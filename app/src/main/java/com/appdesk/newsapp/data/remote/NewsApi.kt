package com.appdesk.newsapp.data.remote

import com.appdesk.newsapp.data.model.NewsResponse
import com.appdesk.newsapp.utils.Constants.COUNTRY
import com.appdesk.newsapp.utils.Constants.FROM
import com.appdesk.newsapp.utils.Constants.POPULAR_NEWS
import com.appdesk.newsapp.utils.Constants.Q
import com.appdesk.newsapp.utils.Constants.QUERY_API
import com.appdesk.newsapp.utils.Constants.SORT_BY
import com.appdesk.newsapp.utils.Constants.TO
import com.appdesk.newsapp.utils.Constants.TODAY_NEWS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    //get popular news accepting parameter, country and api key
    @GET(POPULAR_NEWS)
    suspend fun getPopularNews(
        @Query(COUNTRY)
        country: String,
        @Query(QUERY_API)
        apiKey: String
    ): Response<NewsResponse>

    //get today news accepting parameter, query, from date, to date, sort by, and api key
    @GET(TODAY_NEWS)
    suspend fun getTodayNews(
        @Query(Q)
        query: String,
        @Query(FROM)
        fromDate: String,
        @Query(TO)
        toDate: String,
        @Query(SORT_BY)
        sortBy: String,
        @Query(QUERY_API)
        apiKey: String
    ): Response<NewsResponse>
}