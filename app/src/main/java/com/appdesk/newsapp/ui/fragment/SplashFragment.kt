package com.appdesk.newsapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appdesk.newsapp.R
import com.appdesk.newsapp.databinding.FragmentSplashBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SplashFragment : Fragment(), CoroutineScope {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentSplashBinding.inflate(layoutInflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch {
            delay(3000)
            withContext(Dispatchers.Main){
                findNavController().navigate(R.id.action_splashFragment_to_popularNewsFragment)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
}

