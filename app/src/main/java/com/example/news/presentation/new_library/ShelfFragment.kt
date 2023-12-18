package com.example.news.presentation.new_library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentShelfBinding
import com.example.news.domain.model.New
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShelfFragment : Fragment(R.layout.fragment_shelf) {
    private lateinit var binding: FragmentShelfBinding
    private lateinit var viewModel: LibraryViewModel
    private lateinit var adaptor: ShelfAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShelfBinding.bind(view)
        viewModel = ViewModelProvider(requireParentFragment())[LibraryViewModel::class.java]
        val bundle = arguments?.takeIf { it.containsKey(ARG_OBJECT) }
        val key = bundle?.getInt(ARG_OBJECT)
        when(key) {
            0 -> adaptor = ShelfAdaptor(
                { new: New -> findNavController().navigate(LibraryFragmentDirections.actionGlobalDetailFragment(new)) },
                { new: New -> viewModel.deleteFromBookmark(new) }
            )
            1 -> adaptor = ShelfAdaptor(
                { new: New -> findNavController().navigate(LibraryFragmentDirections.actionGlobalDetailFragment(new)) },
                { new: New -> viewModel.deleteFromFav(new) }
            )
        }
        binding.libraryList.adapter = adaptor
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { list ->
                    when(key) {
                        0 -> adaptor.submitList(list.filter { it.isBookmark })
                        1 -> adaptor.submitList(list.filter { it.isFav })
                    }
                }
            }
        }
    }

//    private fun setSwipeToDelete(list: List<New>) {
//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.bindingAdapterPosition
//                val new = list[position]
//                adaptor.notifyItemRemoved(position)
//                Snackbar.make(binding.libraryList, "", Snackbar.LENGTH_SHORT).setAction("Undo") {
//                    adaptor.notifyItemInserted(position)
//                }.addCallback(object: Snackbar.Callback() {
//                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
//                        super.onDismissed(transientBottomBar, event)
//                        viewModel.deleteFromLibrary(new)
//                    }
//                }).show()
//            }
//        }).attachToRecyclerView(binding.libraryList)
//    }
}