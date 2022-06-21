package com.appdesk.newsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appdesk.newsapp.R
import com.appdesk.newsapp.databinding.FragmentBookmarkedBinding
import com.appdesk.newsapp.ui.activity.MainActivity
import com.appdesk.newsapp.utils.Constants.BOOKMARKED_MSG
import com.appdesk.newsapp.utils.Constants.ARTICLE_REMOVE_MSG
import com.appdesk.newsapp.viewmodel.NewsViewModel
import com.appdesk.newsapp.utils.newsAdapter
import com.appdesk.newsapp.utils.setupRecyclerView
import com.appdesk.newsapp.utils.shareNews
import com.appdesk.newsapp.utils.snakbar
import kotlin.random.Random


class BookmarkedFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkedBinding
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
        binding = FragmentBookmarkedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView(binding.rvBookmarkNews,requireContext())
        //receive data object through method from adapter and send to news details fragment
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(R.id.action_bookmarkedFragment_to_newsDetailsFragment,bundle)
        }

        //article bookmark in database
        newsAdapter.setOnBookmarkButtonClickListener {
            if(it.id == null){
                it.id = Random.nextInt(0,1000)
            }
            viewModel.bookmarkArticle(it)
            view.snakbar(BOOKMARKED_MSG)
        }

        //article removed from database
        newsAdapter.onArticleDeleteClickListener {
            viewModel.deleteArticle(it)
            view.snakbar(ARTICLE_REMOVE_MSG)
        }

        //share news to other app
        newsAdapter.setOnShareButtonClickListener {
            shareNews(context,it)
        }

        viewModel.getBookmarkedNews().observe(viewLifecycleOwner){ articles ->
            newsAdapter.submitList(articles)
        }
    }







}