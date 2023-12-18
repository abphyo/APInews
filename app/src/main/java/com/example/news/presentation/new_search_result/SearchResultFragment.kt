package com.example.news.presentation.new_search_result

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentSearchResultBinding
import com.example.news.domain.model.New
import com.example.news.presentation.utils.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultFragment : Fragment(R.layout.fragment_search_result) {
    private lateinit var binding: FragmentSearchResultBinding
    private val viewModel: SearchPrepViewModel by activityViewModels()
    private val searchResultAdaptor = SearchResultAdaptor(
        { new: New -> findNavController().navigate(SearchResultFragmentDirections.actionGlobalDetailFragment(new)) },
        { new: New -> viewModel.bookmarkNew(new) },
        { new: New -> viewModel.unBookmarkNew(new) }
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchResultBinding.bind(view)
        binding.resultList.adapter = searchResultAdaptor
        binding.statusText.isVisible = false
        binding.noNetworkIcon.isVisible = false
        binding.chromeDino.isVisible = false
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.networkStatus.collectLatest {
                when(it) {
                    ConnectivityObserver.Status.UNAVAILABLE -> {
                        renderError(getString(R.string.no_network_connection))
                    }
                    ConnectivityObserver.Status.LOST -> {
                        renderError(getString(R.string.connection_lost))
                    }
                    else -> renderResult()
                }
            }
        }
    }

    private fun renderResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    with(it) {
                        when {
                            isLoading -> loading()
                            error.isNotEmpty() -> renderError(error)
                            uiList.isNotEmpty() -> renderSuccess(uiList)
                            uiList.isEmpty() -> renderNotFound()
                        }
                    }
                }
            }
        }
    }

    private fun loading() {
        binding.resultProgressIndicator.isVisible = true
        binding.noNetworkIcon.isVisible = false
        binding.statusText.isVisible = false
    }

    private fun renderError(error: String) {
        binding.resultLayout.isVisible = false
        binding.resultProgressIndicator.isVisible = false
        if (error == "Unexpected error occurred!") {
            binding.noNetworkIcon.setImageIcon(null)
        } else {
            binding.chromeDino.isVisible = true
            binding.chromeDino.setOnClickListener {
                playGame()
            }
        }
        binding.statusText.text = error
        binding.statusText.isVisible = true
    }

    private fun renderSuccess(list: List<New>) {
        binding.resultProgressIndicator.isVisible = false
        binding.noNetworkIcon.isVisible = false
        binding.statusText.isVisible = false
        searchResultAdaptor.submitList(list)
        binding.resultLayout.isVisible = true
    }

    private fun renderNotFound() {
        binding.resultLayout.isVisible = false
        binding.statusText.text = getString(R.string.query_not_found)
        binding.noNetworkIcon.isVisible = false
        binding.resultProgressIndicator.isVisible = false
        binding.statusText.isVisible = true
    }

    private fun playGame() {
        findNavController().navigate(SearchResultFragmentDirections.actionGlobalGameFragment())
    }
}