package com.example.news.presentation.new_search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentSearchBinding
import com.example.news.presentation.model.CategoryType
import com.example.news.presentation.utils.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), CategoryGridAdaptor.OnItemClickListener {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        // Add decoration
        val spacingInPixels = (16 * resources.displayMetrics.density).toInt()
        binding.gridView.addItemDecoration(GridItemDecoration(2, spacingInPixels))

        val categoryGridAdaptor = CategoryGridAdaptor(
            itemClickListener = this
        )
        categoryGridAdaptor.submitList(CategoryType.values().toList())
        binding.gridView.adapter = categoryGridAdaptor

        binding.searchCard.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultFragment())
        }
    }

    override fun onItemClick(categoryType: CategoryType) {
        viewModel.updatePublishersUi(categoryType)
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToPublishersFragment())
    }
}