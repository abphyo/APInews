package com.example.news.presentation.new_publishers

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.news.R
import com.example.news.databinding.FragmentPublishersItemBinding
import com.example.news.domain.model.Publisher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PublishersAdapter(

) : ListAdapter<Publisher, PublishersAdapter.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentPublishersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val publisher = getItem(position)
        holder.binding.apply {
            with(publisher) {
                CoroutineScope(Dispatchers.Main).launch {
                    iconImage.layout(0, 0 , 0, 0)
                    Glide.with(iconImage)
                        .load("$url/favicon.ico")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.icon_baseline_image_50)
                        .error(R.drawable.icon_baseline_broken_image_50)
                        .into(iconImage)
                }
                nameText.text = name
                descriptionText.text = description
                categoryText.text = category
                countryText.text = country
            }
        }
    }

    inner class ViewHolder(val binding: FragmentPublishersItemBinding): RecyclerView.ViewHolder(binding.root)

    class DiffCallBack : DiffUtil.ItemCallback<Publisher>() {
        override fun areItemsTheSame(oldItem: Publisher, newItem: Publisher): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Publisher, newItem: Publisher): Boolean {
            return oldItem == newItem
        }
    }
}