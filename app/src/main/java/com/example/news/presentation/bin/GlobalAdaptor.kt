package com.example.news.presentation.bin

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

class GlobalAdaptor<T, VB: ViewBinding>(
    private val itemClickListener : OnItemClickListener<T>,
    private val adaptorImpl: AdaptorContract<T, VB>
) : ListAdapter<T, GlobalAdaptor<T, VB>.ViewHolder<VB>>(
    DiffCallBack<T>(
        adaptorImpl.itemsTheSame,
        adaptorImpl.contentsTheSame
    )
) {

    interface AdaptorContract<T, VB: ViewBinding> {
        fun provideBinding(parent: ViewGroup): VB
        fun bindView(new: T, binding: VB, listener: OnItemClickListener<T>)
        val itemsTheSame: (oldItem: T, newItem: T) -> Boolean
        val contentsTheSame: (oldItem: T, newItem: T) -> Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        return ViewHolder(
            adaptorImpl.provideBinding(parent)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        val item = getItem(position)
        adaptorImpl.bindView(item, holder.binding, itemClickListener)
    }

    inner class ViewHolder<VB: ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    class DiffCallBack<T>(
        private val n1: (oldItem: T, newItem: T) -> Boolean,
        private val n2: (oldItem: T, newItem: T) -> Boolean
    ) : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
            return n1(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
            return n2(oldItem, newItem)
        }
    }

}