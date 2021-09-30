package com.yolo.yolo_android.ui.home_list

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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

    @Inject
    lateinit var factory: HomeListViewModel.HomeListViewModelFactory

    private val viewModel: HomeListViewModel by viewModels {
        val areaCode = arguments?.getInt(AREA_NUMBER)
        val contentTypeId = arguments?.getInt(CATEGORY_NUMBER)
        HomeListViewModel.provideFactory(factory, areaCode ?: -1, if (contentTypeId == -1) null else contentTypeId)
    }

    private var mSelectedType = HomeListFilter.OPTION_01

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
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

            val data = FilterListData("정렬 방법", HomeListFilter::class.java, mSelectedType)
            val dialog = FilterBottomDialog.newInstance(data) {
                if (it == "dismiss") {
                    rotateFilterArrow(true, binding.ivHomeListFilterDropdown)
                }else {
                    mSelectedType = HomeListFilter.valueOf(it)
                    binding.tvHomeListFilter.text = mSelectedType.options
                }
            }
            dialog.show(childFragmentManager, dialog.tag)

        }

        return root
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