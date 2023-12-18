package com.example.news.presentation.new_publishers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.news.R
import com.example.news.databinding.FragmentPublishersBinding
import com.example.news.domain.model.Publisher
import com.example.news.presentation.utils.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PublishersFragment : Fragment(R.layout.fragment_publishers) {

    private lateinit var binding: FragmentPublishersBinding

    private val viewModel: SearchViewModel by activityViewModels()

    private val adaptor: PublishersAdapter = PublishersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPublishersBinding.bind(view)
        binding.publisherList.adapter = adaptor
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sourceUiState.collectLatest {
                    println("check: ${it.uiList}")
                    with(it) {
                        when {
                            isLoading -> binding.progressIndicator.isVisible = true
                            error.isNotEmpty() -> renderError(error)
                            uiList.isNotEmpty() -> updateUi(uiList)
                        }
                    }
                }
            }
        }
    }

    private fun updateUi(list: List<Publisher>) {
        binding.progressIndicator.isVisible = false
        adaptor.submitList(list)
    }

    private fun renderError(error: String) {
        binding.progressIndicator.isVisible = false
        adaptor.submitList(emptyList())
        Snackbar.make(requireView(), error, Snackbar.LENGTH_SHORT).show()
    }

}