package com.example.news.presentation.new_search_prep

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.news.R
import com.example.news.databinding.FragmentFilterDialogBinding

class FilterDialogFragment: DialogFragment(R.layout.fragment_filter_dialog) {

    private lateinit var binding: FragmentFilterDialogBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}