package com.example.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.news.databinding.FragmentGameBinding

class GameFragment : Fragment(R.layout.fragment_game) {

    private lateinit var binding: FragmentGameBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)
        with(binding.gameWebView) {
            loadUrl("file:///android_asset/index.html")
            settings.javaScriptEnabled = true
        }
    }
}