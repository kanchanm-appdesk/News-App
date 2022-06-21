package com.appdesk.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appdesk.newsapp.data.model.Article
import com.appdesk.newsapp.data.model.NewsResponse
import com.appdesk.newsapp.data.repository.NewsRepository
import com.appdesk.newsapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    //create a live data object for popular news
    val popularNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var country = "in"

    //create a live data object for today news
    val todayNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    //get current date
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val currentDate = sdf.format(Date())
    private var query = "india"
    private var sortBy = "publishedAt"

    init {
        getPopularNews(country)
        getTodayNews(query,currentDate,currentDate,sortBy)
    }

    //get response of popular news from repository
    private fun getPopularNews(country:String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            popularNewsLiveData.postValue(Resource.Loading())
            val response = newsRepository.getPopularNews(country)
            popularNewsLiveData.postValue(handleResponse(response))
        }
    }

    //get response of today news from repository
    private fun getTodayNews(query: String,fromDate: String,toDate: String,sortBy : String) =
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                todayNewsLiveData.postValue(Resource.Loading())
                val response = newsRepository.getTodayNews(query,fromDate,toDate,sortBy)
                todayNewsLiveData.postValue(handleResponse(response))
            }
        }

    //handle response of data
    private fun handleResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    //bookmark article in database from news detail fragment
    fun bookmarkArticle(article: Article) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            newsRepository.insertAndUpdateArticle(article)
        }
    }

    //get all bookmarked article
    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()

    //delete article
    fun deleteArticle(article: Article) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            newsRepository.deleteArticle(article)
        }
    }
}