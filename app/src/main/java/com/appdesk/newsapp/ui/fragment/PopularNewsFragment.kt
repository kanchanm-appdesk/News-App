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
import com.appdesk.newsapp.databinding.FragmentPopularNewsBinding
import com.appdesk.newsapp.ui.activity.MainActivity
import com.appdesk.newsapp.utils.*
import com.appdesk.newsapp.utils.Constants.POPULAR_TAG
import com.appdesk.newsapp.viewmodel.NewsViewModel
import kotlin.random.Random


class PopularNewsFragment : Fragment() {

    private lateinit var binding: FragmentPopularNewsBinding
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
        binding = FragmentPopularNewsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView(binding.rvPopularNews, requireContext())
        fetchPopularNews()


        //receive data object through higher order function from adapter
        // and send same to news details fragment
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article", it)
            }
            findNavController().navigate(
                R.id.action_popularNewsFragment_to_newsDetailsFragment,
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
    private fun fetchPopularNews() {
        viewModel.popularNewsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBarPopularNews.hide()
                    response.data?.let { newsResponse ->
                        newsAdapter.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    binding.progressBarPopularNews.hide()
                    response.message?.let { message ->
                        Log.e(POPULAR_TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarPopularNews.show()
                }
            }
        }
    }


}