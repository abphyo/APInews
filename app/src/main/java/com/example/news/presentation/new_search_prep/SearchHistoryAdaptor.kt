package com.example.news.presentation.new_search_prep

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.FragmentSearchHistoryItemBinding

class SearchHistoryAdaptor(
    private val list: List<String>,
    private val delete: (String) -> Unit,
    private val submit: (String) -> Unit
): RecyclerView.Adapter<SearchHistoryAdaptor.ViewHolder>() {

    inner class ViewHolder(val binding: FragmentSearchHistoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentSearchHistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            queryText.text = list[position]
            root.setOnClickListener {
                submit(list[position])
            }
            deleteIcon.setOnClickListener {
                delete(list[position])
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun removeItem(position: Int) {
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }
}