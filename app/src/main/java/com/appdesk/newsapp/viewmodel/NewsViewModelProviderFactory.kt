package com.appdesk.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdesk.newsapp.data.repository.NewsRepository

//Create object of Repository class for viewModel
class NewsViewModelProviderFactory(private val newsRepository: NewsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}