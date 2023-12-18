package com.example.news.presentation.bin

import androidx.viewbinding.ViewBinding
import com.example.news.domain.model.New

abstract class DefaultNewAdaptorImpl<VB: ViewBinding> : GlobalAdaptor.AdaptorContract<New, VB> {
    override val itemsTheSame: (oldItem: New, newItem: New) -> Boolean
        get() = { oldItem, newItem -> oldItem.url == newItem.url }
    override val contentsTheSame: (oldItem: New, newItem: New) -> Boolean
        get() = { oldItem, newItem -> oldItem == newItem }
}

interface OnItemClickListener<T> {
    fun onItemClick(new: T)
    fun saveNew(new: New) {}
    fun unSaveNew(new: New) {}
}
