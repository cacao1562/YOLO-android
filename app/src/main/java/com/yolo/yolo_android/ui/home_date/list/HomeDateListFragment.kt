package com.yolo.yolo_android.ui.home_date.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeDateListBinding
import com.yolo.yolo_android.dpToPx
import com.yolo.yolo_android.model.FilterListData
import com.yolo.yolo_android.rotateFilterArrow
import com.yolo.yolo_android.ui.dialog.FilterBottomDialog
import com.yolo.yolo_android.ui.home_area.list.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeDateListFragment: BindingFragment<FragmentHomeDateListBinding>(R.layout.fragment_home_date_list) {

    companion object {

        const val SELECTED_DATE = "SELECTED_DATE"
        const val CATEGORY_NUMBER = "CATEGORY_NUMBER"

        fun newInstance(selectedDate: String, contentTypeId: Int): HomeDateListFragment {
            val fragment = HomeDateListFragment()
            fragment.apply {
                arguments = bundleOf(SELECTED_DATE to selectedDate, CATEGORY_NUMBER to contentTypeId)
            }
            return fragment
        }
    }

    private var mFilterType: HomeDateListFilter = HomeDateListFilter.OPTION_01

    @Inject
    lateinit var factory: HomeDateListViewModel.HomeDateListViewModelFactory

    private val viewModel: HomeDateListViewModel by viewModels {
        val selectedDate = arguments?.getString(SELECTED_DATE)
        val contentTypeId = arguments?.getInt(CATEGORY_NUMBER)
        HomeDateListViewModel.provideFactory(
            factory,
            selectedDate ?: "",
            if (contentTypeId == -1) null else contentTypeId
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        binding.tvHomeListFilter.text = mFilterType.options

        val pagingAdapter = HomeDateListPagingAdapter()
        binding.rvHomeList.apply {
            adapter = pagingAdapter
            setHasFixedSize(true)
            addItemDecoration(HomeListDecoration(20.dpToPx()))
        }
        lifecycleScope.launchWhenCreated {
//            viewModel.listData.collectLatest {
//                pagingAdapter.submitData(it)
//            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.tvHomeListCount.text = "총 ${pagingAdapter.itemCount}"
            }
        }

        binding.llHomeListFilter.setOnClickListener {

            rotateFilterArrow(false, binding.ivHomeListFilterDropdown)

            val data = FilterListData("정렬 방법", HomeDateListFilter::class.java, mFilterType)
            val dialog = FilterBottomDialog.newInstance(data) {
                if (it == "dismiss") {
                    rotateFilterArrow(true, binding.ivHomeListFilterDropdown)
                }else {
                    mFilterType = HomeDateListFilter.valueOf(it)
                    binding.tvHomeListFilter.text = mFilterType.options
                    viewModel.setCongestion(mFilterType.sortBy)
                    pagingAdapter.refresh()
                }
            }
            dialog.show(childFragmentManager, dialog.tag)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                Log.d("error addLoadStateListener", "${loadStates}")
                binding.llHomeListError.isVisible = loadStates.source.refresh is LoadState.Error
                viewModel._isLoading.value = loadStates.source.append is LoadState.Loading
                val errorState = loadStates.source.refresh as? LoadState.Error
                    ?: loadStates.refresh as? LoadState.Error
                    ?: loadStates.append as? LoadState.Error
                    ?: loadStates.prepend as? LoadState.Error
                errorState?.let {
                    if (!it.error.message.isNullOrEmpty()) {
                        viewModel._toastMessage.emit(it.error.message!!)
                    }
                }

            }
        }

        binding.btnHomeListRetry.setOnClickListener {
            pagingAdapter.retry()
        }

        return root
    }

}

enum class HomeDateListFilter(val options: String, val sortBy: String) {
    OPTION_01("혼잡도 낮은순", "low"),
    OPTION_02("혼잡도 높은순", "high");

    override fun toString(): String {
        return options.toString()
    }
}