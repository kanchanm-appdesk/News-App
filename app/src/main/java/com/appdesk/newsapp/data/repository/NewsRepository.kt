package com.appdesk.newsapp.data.repository

import com.appdesk.newsapp.data.local.ArticleDatabase
import com.appdesk.newsapp.data.model.Article
import com.appdesk.newsapp.data.model.NewsResponse
import com.appdesk.newsapp.data.remote.Retrofit
import com.appdesk.newsapp.utils.Constants.API_KEY
import retrofit2.Response

class NewsRepository(private val articleDatabase: ArticleDatabase) {

    //get popular news response from interface get call
    suspend fun getPopularNews(country: String): Response<NewsResponse> =
        Retrofit.getInstance.getPopularNews(country, API_KEY)

    //get today news response from interface get call
    suspend fun getTodayNews(
        query: String,
        fromDate: String,
        toDate: String,
        sortBy: String
    ): Response<NewsResponse> =
        Retrofit.getInstance.getTodayNews(query, fromDate, toDate, sortBy, API_KEY)


    suspend fun insertAndUpdateArticle(article: Article) =
        articleDatabase.getArticleDao().insertAndUpdateArticle(article)

    fun getBookmarkedNews() = articleDatabase.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = articleDatabase.getArticleDao().deleteArticle(article)


}