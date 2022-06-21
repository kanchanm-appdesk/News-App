package com.appdesk.newsapp.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdesk.newsapp.ui.adapter.NewsAdapter
import com.appdesk.newsapp.data.model.Article
import com.google.android.material.snackbar.Snackbar

val newsAdapter = NewsAdapter()

/// share news to other app
fun shareNews(context: Context?, article: Article){
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, article.urlToImage)
        putExtra(Intent.EXTRA_STREAM, article.urlToImage)
        putExtra(Intent.EXTRA_TITLE, article.title)
        type ="image/*"
    }
    context?.startActivity(Intent.createChooser(intent, "Share News"))
}

//for show ui components visibility
fun View.show(){
    visibility = View.VISIBLE
}

//for hide ui components visibility
fun View.hide(){
    visibility = View.GONE
}

//for using display message to user
fun View.snakbar(message:String){
    Snackbar.make(this,message,Snackbar.LENGTH_SHORT).show()
}

//for using set recycler view of fragments
fun setupRecyclerView(id: RecyclerView,context: Context){
    id.adapter = newsAdapter
    id.layoutManager = LinearLayoutManager(context)
}

fun TextView.setTextInTextView(text: String){
    this.text = text
}

fun Context.isConnected(): Boolean {

    // register activity with the connectivity manager service
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // NetworkCapabilities to check what type of network has the internet connection
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        // Returns a Network object
        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    } else {
        // if the android version is below M
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}