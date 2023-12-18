package com.example.news.presentation.new_headlines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentHeadlinesBinding
import com.example.news.domain.model.New
import com.example.news.presentation.model.CategoryType
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    private lateinit var binding: FragmentHeadlinesBinding

    private val viewModel: HeadlinesViewModel by viewModels()

    private val headlinesAdaptor = HeadlinesAdaptor(
        { new: New -> findNavController().navigate(HeadlinesFragmentDirections.actionGlobalDetailFragment(new)) },
        { new: New -> viewModel.bookmarkNew(new) },
        { new: New -> viewModel.unBookmarkNew(new) }
    )

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentItemListBinding.inflate(inflater, container, false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)
        binding.list.adapter = headlinesAdaptor
        initUi()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    with(it) {
                        when {
                            isLoading -> loadingUi()
                            error.isNotEmpty() -> renderError(error)
                            uiList.isNotEmpty() -> renderSuccess(uiList)
                        }
                    }
                }
            }
        }
    }

//    private fun testUi() {
//        val mockList: List<New> = listOf(
//            New(
//                "Northern Greenland's ice shelves are declining, accelerating sea level rise - The Washington Post - The Washington Post",
//                "https://www.washingtonpost.com/climate-environment/2023/11/07/northern-greenland-ice-shelves-decline/",
//                "https://www.washingtonpost.com/wp-apps/imrs.php?src=https://arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/RHPUBDI62ELUVZOPIFJ25DNASA_size-normalized.jpg&w=1440",
//                "2023-11-07T16:29:43Z"
//                )
//        )
//        recyclerAdapter.submitList(mockList)
//    }

    private fun initUi() {
        val categories = CategoryType.values().map { it.title }
        with(binding.lyTabHeadlines) {
            categories.forEach {
                addTab(newTab().setText(it))
            }
            addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewModel.updateUiByCategory(CategoryType.values()[tab?.position ?: 0])
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.category.collectLatest {
                        selectTab(getTabAt(CategoryType.values().indexOf(it)))
                    }
                }
            }
        }
    }

    private fun loadingUi() {
        progressingUi(true)
        headlinesAdaptor.submitList(emptyList())
    }

    private fun renderSuccess(newList: List<New>) {
        progressingUi(false)
        println("flow1: $newList")
        headlinesAdaptor.submitList(newList)
    }

    private fun renderError(error: String) {
        progressingUi(false)
        headlinesAdaptor.submitList(emptyList())
        Snackbar.make(requireView(), error, Snackbar.LENGTH_SHORT).show()
    }

    private fun progressingUi(visible: Boolean) {
        with(binding) {
            progressIndicator.isVisible = visible
            if (visible) {
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
            } else {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
        }
    }

}