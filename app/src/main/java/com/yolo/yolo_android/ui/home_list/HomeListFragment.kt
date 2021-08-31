package com.yolo.yolo_android.ui.home_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class HomeListFragment: BindingFragment<FragmentHomeListBinding>(R.layout.fragment_home_list) {

    companion object {

        const val CATEGORY_NUMBER = "CATEGORY_NUMBER"

        fun newInstance(number: Int): HomeListFragment {
            val bundle = Bundle()
            bundle.putInt(CATEGORY_NUMBER, number)
            val fragment = HomeListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var factory: HomeListViewModel.HomeListViewModelFactory

    private val viewModel: HomeListViewModel by viewModels {
        val id = arguments?.getInt(CATEGORY_NUMBER)
        HomeListViewModel.provideFactory(factory, if (id == -1) null else id)
    }

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
        }
        lifecycleScope.launchWhenCreated {
            viewModel.listData.collectLatest {
                homeListPagingAdapter.submitData(it)
            }
        }

        return root
    }

}