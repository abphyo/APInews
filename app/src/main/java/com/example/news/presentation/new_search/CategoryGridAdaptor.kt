package com.example.news.presentation.new_search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.GridviewCategoryItemBinding
import com.example.news.presentation.model.CategoryType
import kotlin.random.Random

class CategoryGridAdaptor(
    private val itemClickListener: OnItemClickListener
): ListAdapter<CategoryType, CategoryGridAdaptor.ViewHolder>(DiffCallBack()) {

    interface OnItemClickListener {
        fun onItemClick(categoryType: CategoryType)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryGridAdaptor.ViewHolder {
        return ViewHolder(
            GridviewCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryGridAdaptor.ViewHolder, position: Int) {
        val categoryType = getItem(position)
        with(holder.binding) {
            categoryText.text = categoryType.title
            baseCardView.setBackgroundColor(
                Color.argb(
                    255,
                    Random.nextInt(256),
                    Random.nextInt(256),
                    Random.nextInt(256)
                )
            )
            root.setOnClickListener {
                itemClickListener.onItemClick(categoryType)
            }
        }
    }

    inner class ViewHolder(val binding: GridviewCategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    class DiffCallBack : DiffUtil.ItemCallback<CategoryType>() {
        override fun areItemsTheSame(oldItem: CategoryType, newItem: CategoryType): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: CategoryType, newItem: CategoryType): Boolean {
            return oldItem == newItem
        }
    }
}