package com.yolo.yolo_android.ui.community_list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.yolo.yolo_android.CommunitySort
import com.yolo.yolo_android.DialogButtonType
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityListBinding
import com.yolo.yolo_android.model.CallbackPostButton
import com.yolo.yolo_android.ui.dialog.CommonDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class CommunityListFragment: BindingFragment<FragmentCommunityListBinding>(R.layout.fragment_community_list) {

    companion object {
        const val KEY_SORTED = "KEY_SORTED"
        fun newInstance(sorted: CommunitySort): CommunityListFragment {
            val fragment = CommunityListFragment()
            fragment.apply {
                arguments = bundleOf(KEY_SORTED to sorted)
            }
            return fragment
        }
    }

    @Inject
    lateinit var factory: CommunityListViewModel.CommunityListViewModelFactory

    private val viewModel: CommunityListViewModel by viewModels {
        val sorted = arguments?.getSerializable(KEY_SORTED) as CommunitySort
        CommunityListViewModel.provideFactory(factory, sorted)
    }

    lateinit var mAdapter: CommunityListPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initAdapter()
        initSwipeLayout()
        childFragmentManager.setFragmentResultListener(CommonDialog.REQ_RESULT_POST_ID, this) { requestKey, bundle ->
            val postId = bundle.getInt("postId")
            if (postId != -1) viewModel.onViewEvent(CallbackPostButton.Delete(postId))
        }

        viewModel.listData.observe(viewLifecycleOwner, Observer {
            mAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        viewModel.callbackPostBtn.observe(viewLifecycleOwner, Observer {
            when(it) {
                is CallbackPostButton.More -> presentDialog(it.postId)
                is CallbackPostButton.Map -> presentMap(it)
            }
        })

    }

    private fun initAdapter() {
        mAdapter = CommunityListPagingAdapter(viewModel)
        binding.rvCommunityList.apply {
            adapter = mAdapter
            setHasFixedSize(true)
        }
    }

    private fun initSwipeLayout() {
        binding.swipeVCommunityList.setOnRefreshListener {
            mAdapter.refresh()
        }

        lifecycleScope.launchWhenCreated {
            mAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeVCommunityList.isRefreshing = loadStates.mediator?.refresh is LoadState.Loading
            }
        }
    }


    private fun presentDialog(id: Int) {
        val dialog = CommonDialog
            .newInstance(getString(R.string.alert),
                getString(R.string.alert_msg_delete_post),
                DialogButtonType.CancelNConfirm,
                id)
        dialog.show(childFragmentManager, CommonDialog::class.java.simpleName)
    }

    private fun presentMap(callback: CallbackPostButton.Map) {
        val url = "kakaomap://look?p=${callback.latitude},${callback.longitude}"
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).also {
            startActivity(it)
        }
    }

}