//package com.example.news.presentation.bin
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import com.example.news.R
//import com.example.news.databinding.FragmentHeadlinesItemBinding
//import com.example.news.domain.model.New
//import com.squareup.picasso.Picasso
//
//class HeadlinesAdaptorImpl: DefaultNewAdaptorImpl<FragmentHeadlinesItemBinding>() {
//    override fun provideBinding(
//        parent: ViewGroup,
//    ): FragmentHeadlinesItemBinding {
//        return FragmentHeadlinesItemBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//    }
//
//    override fun bindView(
//        new: New,
//        binding: FragmentHeadlinesItemBinding,
//        listener: OnItemClickListener<New>
//    ) {
//        with(binding) {
//            newTitle.text = new.title
//            if (new.urlToImage.isNotEmpty()) {
//                Picasso.get()
//                    .load(new.urlToImage)
//                    .fit()
//                    .centerCrop()
//                    .priority(Picasso.Priority.HIGH)
//                    .placeholder(R.drawable.image_placeholder)
//                    .error(R.drawable.image_placeholder)
//                    .into(newImage)
//            }
//            publishedAt.text = new.publishedAt
//            bookmarkImage.apply {
//                setImageResource(
//                    if (new.isBookmark) R.drawable.bookmark_fill_24px
//                    else R.drawable.bookmark_24px
//                )
//                setOnClickListener {
//                    if (new.isBookmark) listener.unSaveNew(new)
//                    else listener.saveNew(new)
//                }
//            }
//            root.setOnClickListener {
//                listener.onItemClick(new)
//            }
//        }
//    }
//}