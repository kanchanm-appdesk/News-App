package com.appdesk.newsapp.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appdesk.newsapp.R
import com.appdesk.newsapp.databinding.ItemNewsCardViewBinding
import com.appdesk.newsapp.data.model.Article
import com.appdesk.newsapp.utils.setTextInTextView
import com.bumptech.glide.Glide

class NewsAdapter : ListAdapter<Article, NewsAdapter.ArticleViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemNewsCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        val bookmarked = "Bookmarked"
        val bookmark = "Bookmark"
        holder.binding.apply {

            Glide.with(root).load(article.urlToImage).into(ivPoster)

            tvTitle.setTextInTextView(article.title!!)
            tvDescription.setTextInTextView(article.description!!)
            tvSource.setTextInTextView(article.source!!.name)
            tvPublishedAt.setTextInTextView(article.publishedAt!!)

            articleCard.setOnClickListener { onItemClickListener?.let { it(article) } }

            ivBtnBookmark.setOnClickListener {
                when {
                    ivBtnBookmark.tag.equals(bookmarked) -> {
                        updateBookmark(holder,bookmark,ContextCompat.getDrawable(holder.itemView.context,R.drawable.ic_bookmark))
                        onArticleDeleteClick?.let { it(article) }

                    }
                    else -> {
                        updateBookmark(holder,bookmarked,ContextCompat.getDrawable(holder.itemView.context,R.drawable.ic_bookmarked))
                        onBookmarkButtonClickListener?.let { it(article) }
                    }
                }

            }
            ivBtnShare.setOnClickListener { onShareButtonClickListener?.let { it(article) } }
        }
    }

    private fun updateBookmark(holder: ArticleViewHolder, bookmark: String, drawable: Drawable?) {

        holder.binding.apply {
            ivBtnBookmark.tag = bookmark
            ivBtnBookmark.setImageDrawable(drawable)
        }

    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    private var onBookmarkButtonClickListener: ((Article) -> Unit)? = null
    private var onShareButtonClickListener: ((Article) -> Unit)? = null
    private var onArticleDeleteClick: ((Article) -> Unit?)? = null


    fun setOnBookmarkButtonClickListener(listener: (Article) -> Unit) {
        onBookmarkButtonClickListener = listener
    }

    fun setOnShareButtonClickListener(listener: (Article) -> Unit) {
        onShareButtonClickListener = listener
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    fun onArticleDeleteClickListener(listener: (Article) -> Unit) {
        onArticleDeleteClick = listener
    }

    //Compare oldArticle Data with newArticle Data for improve recycler view performance
    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    inner class ArticleViewHolder(val binding: ItemNewsCardViewBinding) :
        RecyclerView.ViewHolder(binding.root)

}
