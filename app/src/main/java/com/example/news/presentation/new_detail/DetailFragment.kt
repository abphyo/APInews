package com.example.news.presentation.new_detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.news.R
import com.example.news.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail), MenuProvider {

    private lateinit var binding: FragmentDetailBinding

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        binding.webProgress.isVisible = true
        (requireActivity() as MenuHost).addMenuProvider(
            this,
            viewLifecycleOwner
        )
        setUpWebView(args.argument.url)
        binding.favBtn.apply {
            setImageResource(
                if (args.argument.isFav) R.drawable.favorite_fill_24px else R.drawable.favorite_24px
            )
            setOnClickListener {
                if (args.argument.isFav) {
                    viewModel.handleFav(args.argument, false)
                    setImageResource(R.drawable.favorite_24px)
                }
                else {
                    viewModel.handleFav(args.argument, true)
                    setImageResource(R.drawable.favorite_fill_24px)
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_detail_menu, menu)
        val bookmarkItem = menu.findItem(R.id.action_bookmark)
        if (args.argument.isBookmark) bookmarkItem.setIcon(R.drawable.bookmark_fill_24px)
        else bookmarkItem.setIcon(R.drawable.bookmark_24px)
        bookmarkItem.setOnMenuItemClickListener {
            if (args.argument.isBookmark) viewModel.handleBookmark(args.argument, false)
            else viewModel.handleBookmark(args.argument, true)
            true
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    private fun setUpWebView(url: String) {
        binding.webView.apply {
            loadUrl(url)
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.webProgress.isVisible = false
                }
            }
        }

    }


}