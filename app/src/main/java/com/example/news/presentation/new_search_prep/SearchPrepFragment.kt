package com.example.news.presentation.new_search_prep

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentSearchPrepBinding
import com.example.news.presentation.model.SearchInType
import com.example.news.presentation.utils.ConnectivityObserver
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchPrepFragment :
    Fragment(R.layout.fragment_search_prep), MenuProvider {
    private lateinit var binding: FragmentSearchPrepBinding
    private lateinit var searchView: SearchView
    private val viewModel: SearchPrepViewModel by activityViewModels()
    private lateinit var searchHistoryAdaptor: SearchHistoryAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchPrepBinding.bind(view)
        (requireActivity() as MenuHost).addMenuProvider(
            this,
            viewLifecycleOwner
        )
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true
        binding.historyList.layoutManager = layoutManager
        with(binding.searchInChipGroup) {
            SearchInType.values().forEach {
                addView(createChip(it, viewModel.searchIn.value.contains(it.searchIn)))
            }
        }
        activateFilter()
        checkHistory()
        checkNetwork()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Query"
        searchView.isIconified = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.filterLayout.isVisible = false
                binding.historyLayout.isVisible = false
                searchView.clearFocus()
                viewModel.updateResultEverything(query ?: "")
                viewModel.pushToQueue(query ?: "")
                findNavController().navigate(
                    SearchPrepFragmentDirections.actionSearchPrepFragmentToSearchResultFragment()
                )
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId) {
            R.id.action_search -> true
            else -> false
        }
    }

    private fun activateFilter() {
        binding.filterIcon.setOnClickListener {
            FilterDialogFragment().show(childFragmentManager, FilterDialogFragment.TAG)
        }
    }

    private fun checkHistory() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchHistory.collectLatest { state ->
                    println("history: ${state.historyList}")
                    searchHistoryAdaptor = SearchHistoryAdaptor(
                        state.historyList,
                        { item: String -> viewModel.popFromQueue(item) },
                        { query: String -> searchView.setQuery(query, true) }
                    )
                    binding.historyList.adapter = searchHistoryAdaptor
                    binding.historyHeader.isVisible = state.historyList.isNotEmpty()
                }
            }
        }
    }

    private fun checkNetwork() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.networkStatus.collectLatest {
                when(it) {
                    ConnectivityObserver.Status.UNAVAILABLE -> {
                        binding.noNetworkIcon.isVisible = true
                        binding.statusText.text = getString(R.string.no_network_connection)
                    }
                    ConnectivityObserver.Status.LOST -> {
                        binding.noNetworkIcon.isVisible = true
                        binding.statusText.text = getString(R.string.connection_lost)
                    }
                    else -> {
                        binding.noNetworkIcon.isVisible = false
                        binding.statusText.text = getString(R.string.find_what_you_interest)
                    }
                }
            }
        }
    }

    private fun createChip(type: SearchInType, checked: Boolean): Chip {
        val chip = Chip(requireContext())
        chip.text = type.searchIn
        chip.isCheckable = true
        chip.isCheckedIconVisible = false
        chip.isChecked = checked
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.addToSearchIn(type)
            else viewModel.removeFromSearchIn(type)
        }
        return chip
    }
}