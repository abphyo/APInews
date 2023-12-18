package com.example.news.presentation.new_library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.news.R
import com.example.news.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment(R.layout.fragment_library) {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var viewPagerAdaptor: LibraryViewPagerAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLibraryBinding.bind(view)
        viewPagerAdaptor = LibraryViewPagerAdaptor(this)
        initUi()
    }

    private fun initUi() {
        with(binding) {
            binding.libraryViewpager.adapter = viewPagerAdaptor
            TabLayoutMediator(libraryTab, libraryViewpager) { tab , position ->
                when(position) {
                    0 -> {
                        tab.text = "Bookmarks"
                        tab.setIcon(R.drawable.bookmark_item_selector)
                    }
                    1 -> {
                        tab.text = "Favs"
                        tab.setIcon(R.drawable.favourite_item_selector)
                    }
                }
            }.attach()
//            libraryTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    if (tab != null) {
//                        when(tab.position) {
//                            0 -> viewModel.updateUiState(false)
//                            1 -> viewModel.updateUiState(true)
//                        }
//                    }
//                }
//                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                }
//
//                override fun onTabReselected(tab: TabLayout.Tab?) {
//                }
//            })
        }
    }
}