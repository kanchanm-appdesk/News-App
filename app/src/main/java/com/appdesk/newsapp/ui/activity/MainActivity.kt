package com.appdesk.newsapp.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.appdesk.newsapp.R
import com.appdesk.newsapp.data.local.ArticleDatabase
import com.appdesk.newsapp.data.repository.NewsRepository
import com.appdesk.newsapp.databinding.ActivityMainBinding
import com.appdesk.newsapp.utils.hide
import com.appdesk.newsapp.utils.isConnected
import com.appdesk.newsapp.utils.show
import com.appdesk.newsapp.utils.snakbar
import com.appdesk.newsapp.viewmodel.NewsViewModel
import com.appdesk.newsapp.viewmodel.NewsViewModelProviderFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(),CoroutineScope {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    public lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        when {
            this.isConnected() -> {
                val newsRepository = NewsRepository(ArticleDatabase(this))
                val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
                viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
            }
            else -> view.snakbar("Check Internet Connection")
        }

        launch {
            delay(3000)
            withContext(Dispatchers.Main){
                binding.bottomNavigationView.hide()
            }
        }


        //Control navigation bar by using jetpack

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()


}