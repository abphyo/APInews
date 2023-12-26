package com.example.news.presentation.new_search_prep

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.news.R
import com.example.news.databinding.FragmentFilterDialogBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class FilterDialogFragment: DialogFragment(R.layout.fragment_filter_dialog) {

    companion object {
        const val TAG = "FilterDialog"
    }

    private lateinit var binding: FragmentFilterDialogBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterDialogBinding.bind(view)
        val startDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select start date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        startDatePicker.addOnPositiveButtonClickListener {
            binding.fromDate.setText(convertTimeToDate(it))
        }
        val endDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select end date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        endDatePicker.addOnPositiveButtonClickListener {
            binding.toDate.setText(convertTimeToDate(it))
        }
        binding.fromDate.setOnClickListener {
            startDatePicker.show(childFragmentManager, TAG)
        }
        binding.toDate.setOnClickListener {
            endDatePicker.show(childFragmentManager, TAG)
        }

    }

    private fun convertTimeToDate(time: Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(utc.time)
    }

}