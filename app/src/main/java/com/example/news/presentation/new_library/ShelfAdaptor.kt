package com.example.news.presentation.new_library

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.news.R
import com.example.news.databinding.FragmentLibraryItemBinding
import com.example.news.domain.model.New
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShelfAdaptor(
    private val onItemClick: (New) -> Unit,
    private val onDelete: (New) -> Unit
) : ListAdapter<New, ShelfAdaptor.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentLibraryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            with(getItem(position)) {
                CoroutineScope(Dispatchers.Main).launch {
                    Glide.with(libraryImage)
                        .load(urlToImage)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .into(libraryImage)
                }
                libraryTitle.text = title
                libraryDate.text = publishedAt
                delete.setOnClickListener {
                    onDelete(this)
                }
                root.setOnClickListener {
                    onItemClick(this)
                }
            }
        }
    }

    inner class ViewHolder(val binding: FragmentLibraryItemBinding): RecyclerView.ViewHolder(binding.root)

    class DiffCallBack : DiffUtil.ItemCallback<New>() {
        override fun areItemsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem == newItem
        }
    }
}