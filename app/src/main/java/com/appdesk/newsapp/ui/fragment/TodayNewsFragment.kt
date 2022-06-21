package com.appdesk.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appdesk.newsapp.R
import com.appdesk.newsapp.databinding.FragmentTodayNewsBinding
import com.appdesk.newsapp.ui.activity.MainActivity
import com.appdesk.newsapp.viewmodel.NewsViewModel
import com.appdesk.newsapp.utils.*
import kotlin.random.Random


class TodayNewsFragment : Fragment() {

    private lateinit var binding: FragmentTodayNewsBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.moveTaskToBack(true)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTodayNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        fetchTodayNews()
        setupRecyclerView(binding.rvTodayNews, requireContext())


        //receive data object through method from adapter and passed to news details fragment
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article", it)
            }
            findNavController().navigate(
                R.id.action_todayNewsFragment_to_newsDetailsFragment,
                bundle
            )
        }

        //article bookmark in database
        newsAdapter.setOnBookmarkButtonClickListener {
            if(it.id == null){
                it.id = Random.nextInt(0,1000)
            }
            viewModel.bookmarkArticle(it)
            view.snakbar("Article Bookmarked")
        }

        //article removed from database
        newsAdapter.onArticleDeleteClickListener {
            viewModel.deleteArticle(it)
            view.snakbar("Article Removed")
        }

        //share news to other app
        newsAdapter.setOnShareButtonClickListener {
            shareNews(context,it)
        }
    }

    //observe data and set response data into news adapter
    private fun fetchTodayNews() {
        viewModel.todayNewsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBarTodayNews.hide()
                    response.data?.let { newsResponse ->
                        newsAdapter.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    binding.progressBarTodayNews.hide()
                    response.message?.let { message ->
                        Log.e(Constants.TODAY_TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarTodayNews.show()
                }
            }
        }
    }


}