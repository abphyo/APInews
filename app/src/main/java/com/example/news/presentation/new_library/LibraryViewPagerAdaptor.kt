package com.example.news.presentation.new_library

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
const val ARG_OBJECT = "object"
class LibraryViewPagerAdaptor(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = ShelfFragment()
        fragment.arguments = Bundle().apply {
          putInt(ARG_OBJECT, position)
        }
        return fragment
    }
}