package com.yolo.yolo_android.ui.home_list

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeListBinding
import com.yolo.yolo_android.dpToPx
import com.yolo.yolo_android.model.FilterListData
import com.yolo.yolo_android.rotateFilterArrow
import com.yolo.yolo_android.ui.dialog.FilterBottomDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeListFragment: BindingFragment<FragmentHomeListBinding>(R.layout.fragment_home_list) {

    companion object {

        const val AREA_NUMBER = "AREA_NUMBER"
        const val CATEGORY_NUMBER = "CATEGORY_NUMBER"

        fun newInstance(areaCode: Int, contentTypeId: Int): HomeListFragment {
            val bundle = Bundle()
            bundle.putInt(AREA_NUMBER, areaCode)
            bundle.putInt(CATEGORY_NUMBER, contentTypeId)
            val fragment = HomeListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mFilterType: HomeListBaseFilter = HomeListBaseFilter.OPTION_02

    @Inject
    lateinit var factory: HomeListViewModel.HomeListViewModelFactory

    private val viewModel: HomeListViewModel by viewModels {
        val areaCode = arguments?.getInt(AREA_NUMBER)
        val contentTypeId = arguments?.getInt(CATEGORY_NUMBER)
        HomeListViewModel.provideFactory(factory, areaCode ?: -1, if (contentTypeId == -1) null else contentTypeId)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        binding.tvHomeListFilter.text = mFilterType.options

        val homeListPagingAdapter = HomeListPagingAdapter()
        binding.rvHomeList.apply {
            adapter = homeListPagingAdapter
            setHasFixedSize(true)
            addItemDecoration(HomeListDecoration(20.dpToPx()))
        }
        lifecycleScope.launchWhenCreated {
            viewModel.listData.collectLatest {
                homeListPagingAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeListPagingAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.tvHomeListCount.text = "총 ${homeListPagingAdapter.itemCount}"
            }
        }

        binding.llHomeListFilter.setOnClickListener {

            rotateFilterArrow(false, binding.ivHomeListFilterDropdown)

            val data = FilterListData("정렬 방법", HomeListBaseFilter::class.java, mFilterType)
            val dialog = FilterBottomDialog.newInstance(data) {
                if (it == "dismiss") {
                    rotateFilterArrow(true, binding.ivHomeListFilterDropdown)
                }else {
                    mFilterType = HomeListBaseFilter.valueOf(it)
                    binding.tvHomeListFilter.text = mFilterType.options
                    viewModel.setArrage(mFilterType.optionValue)
                    homeListPagingAdapter.refresh()
                }
            }
            dialog.show(childFragmentManager, dialog.tag)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeListPagingAdapter.loadStateFlow.collectLatest { loadStates ->
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
            homeListPagingAdapter.retry()
        }

        return root
    }

}

enum class HomeListBaseFilter(val options: String, val optionValue: String) {
    OPTION_01("제목순", "O"),
    OPTION_02("조회순", "P"),
    OPTION_03("수정일순", "Q"),
    OPTION_04("생성일순", "R");

    override fun toString(): String {
        return options.toString()
    }
    companion object {
        fun from(type: String?): HomeListBaseFilter = HomeListBaseFilter.values().find { it.options == type } ?: HomeListBaseFilter.OPTION_02
    }
}

enum class HomeListFilter(val options: String) {
    OPTION_01("혼잡도 낮은순"),
    OPTION_02("혼잡도 높은순");

    override fun toString(): String {
        return options.toString()
    }
}

class HomeListDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = space
    }

}