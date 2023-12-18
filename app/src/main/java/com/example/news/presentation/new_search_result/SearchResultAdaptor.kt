package com.example.news.presentation.new_search_result

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.news.R
import com.example.news.databinding.FragmentSearchResultItemBinding
import com.example.news.domain.model.New

class SearchResultAdaptor(
    private val onItemClick: (New) -> Unit,
    private val onSave: (New) -> Unit,
    private val onUnSave: (New) -> Unit
) : ListAdapter<New, SearchResultAdaptor.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            FragmentSearchResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val new = getItem(position)
        holder.binding.apply {
            if (new.urlToImage.isNotEmpty()) {
                Glide.with(resultImage)
                    .load(new.urlToImage)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(resultImage)
            }
            resultTitle.text = new.title
            resultDate.text = new.publishedAt
            addToBookmark.apply {
                if (new.isBookmark) {
                    text = context.getString(R.string.bookmarked)
                    setTextColor(context.getColor(R.color.figma_green))
                    bookmarkIcon.setImageResource(R.drawable.check_circle_24px)
                } else {
                    text = context.getString(R.string.add_to_bookmark)
                    setTextColor(context.getColor(R.color.white))
                    bookmarkIcon.setImageResource(R.drawable.add_circle_24px)
                }
                setOnClickListener {
                    if (new.isBookmark) onUnSave(new) else onSave(new)
                }
            }
            root.setOnClickListener {
                onItemClick(new)
            }
        }
    }

    inner class ViewHolder(val binding: FragmentSearchResultItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallBack: DiffUtil.ItemCallback<New>() {
        override fun areItemsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem == newItem
        }

    }

}