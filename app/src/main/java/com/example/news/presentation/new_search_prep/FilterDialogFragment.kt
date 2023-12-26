package com.example.news.presentation.new_search_prep

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.news.R
import com.example.news.databinding.FragmentFilterDialogBinding
import com.example.news.presentation.model.LanguageType
import com.example.news.domain.model.SearchFilter
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class FilterDialogFragment: DialogFragment(R.layout.fragment_filter_dialog) {
    companion object {
        const val TAG = "FilterDialog"
    }
    private lateinit var binding: FragmentFilterDialogBinding
    private lateinit var languageType: LanguageType
    private lateinit var searchFilter: SearchFilter
    private val viewModel: SearchPrepViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterDialogBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filterInstance.collectLatest {
                searchFilter = it
            }
        }
        if (searchFilter.isActive) prefillDate(searchFilter)
        val startDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select start date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        startDatePicker.addOnPositiveButtonClickListener {
            with(convertTimeToDate(it)) {
                binding.fromDate.setText(this)
            }
        }
        val endDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select end date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        endDatePicker.addOnPositiveButtonClickListener {
            with(convertTimeToDate(it)) {
                binding.toDate.setText(this)
            }
        }
        binding.fromDate.setOnClickListener {
            startDatePicker.show(childFragmentManager, TAG)
        }
        binding.toDate.setOnClickListener {
            endDatePicker.show(childFragmentManager, TAG)
        }
        with(binding.languageChipGroup) {
            LanguageType.values().forEach {
                val chip = createChip(it)
                if (it.isoCode == searchFilter.language) chip.isChecked = true
                addView(chip)
            }
        }
        binding.tvFilterCancel.setOnClickListener {
            dismiss()
        }
//        binding.tvFilterApply.isEnabled =
//            searchFilter.from.isNotEmpty() && searchFilter.to.isNotEmpty()
        binding.tvFilterApply.setOnClickListener {
            viewModel.saveFilterInstance(
                SearchFilter(
                    from = binding.fromDate.text.toString(),
                    to = binding.toDate.text.toString(),
                    language = languageType.isoCode,
                    sortBy = "",
                    isActive = true
                )
            )
            dismiss()
        }
    }

    private fun prefillDate(instance: SearchFilter) {
        binding.fromDate.setText(instance.from)
        binding.toDate.setText(instance.to)
    }

    private fun convertTimeToDate(time: Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return format.format(utc.time)
    }

    private fun createChip(type: LanguageType): Chip {
        val chip = layoutInflater.inflate(
            R.layout.custom_chip_layout,
            binding.languageChipGroup,
            false)
                as Chip
        chip.text = type.lgName
        chip.isCheckable = true
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) languageType = type
        }
        return chip
    }

}