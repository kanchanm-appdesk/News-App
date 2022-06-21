package com.appdesk.newsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.appdesk.newsapp.data.model.Article
import com.appdesk.newsapp.utils.Constants.TABLE_NAME

@Dao
interface ArticleDao {

    // insert article into table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdateArticle(article: Article):Long

    //get all articles from database
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllArticles(): LiveData<List<Article>>

    //delete article from database
    @Delete
    suspend fun deleteArticle(article: Article)
}