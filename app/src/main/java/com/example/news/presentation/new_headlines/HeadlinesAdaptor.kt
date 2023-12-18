package com.example.news.presentation.new_headlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.news.R
import com.example.news.databinding.FragmentHeadlinesItemBinding
import com.example.news.domain.model.New
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeadlinesAdaptor(
    private val onItemClick: (New) -> Unit,
    private val onSave: (New) -> Unit,
    private val onUnSave: (New) -> Unit
) : ListAdapter<New, HeadlinesAdaptor.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentHeadlinesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val new = getItem(position)
        holder.binding.apply {
            with(new) {
                newTitle.text = title
                if (urlToImage.isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(newImage)
                            .load(urlToImage)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .into(newImage)
                    }
                }
                date.text = publishedAt
                bookmarkImage.apply {
                    setImageResource(
                        if (isBookmark) R.drawable.bookmark_fill_24px
                        else R.drawable.bookmark_24px
                    )
                    setOnClickListener {
                        if (isBookmark) onUnSave(new)
                        else onSave(new)
                    }
                }
                root.setOnClickListener {
                    onItemClick(new)
                }
            }
        }
    }

    inner class ViewHolder(val binding: FragmentHeadlinesItemBinding): RecyclerView.ViewHolder(binding.root)
    
    class DiffCallBack : DiffUtil.ItemCallback<New>() {
        override fun areItemsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem.url == newItem.url && oldItem.urlToImage == newItem.urlToImage
        }

        override fun areContentsTheSame(oldnew: New, newnew: New): Boolean {
            return oldnew == newnew
        }
    }
}