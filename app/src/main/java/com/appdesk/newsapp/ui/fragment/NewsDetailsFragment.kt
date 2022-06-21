package com.appdesk.newsapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.appdesk.newsapp.databinding.FragmentNewsDetailsBinding
import com.appdesk.newsapp.ui.activity.MainActivity
import com.appdesk.newsapp.utils.setTextInTextView
import com.appdesk.newsapp.viewmodel.NewsViewModel
import com.bumptech.glide.Glide


class NewsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailsBinding
    private lateinit var viewModel: NewsViewModel
    private val args: NewsDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewsDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = args.article
        binding.apply {
            Glide.with(root).load(article.urlToImage).into(ivPosterDetail)

            tvTitleDetail.setTextInTextView(article.title!!)
            tvDescriptionDetail.setTextInTextView(article.description)
            tvContentDetail.setTextInTextView(article.content)
            tvSourceDetail.setTextInTextView(article.source!!.name)
            tvPublishAtDetail.setTextInTextView(article.publishedAt)
        }
    }



}