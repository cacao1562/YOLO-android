package com.yolo.yolo_android.ui.mypage.myspot

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.yolo.yolo_android.DialogButtonType
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentMySpotBinding
import com.yolo.yolo_android.dpToPx
import com.yolo.yolo_android.ui.dialog.CommonDialog
import com.yolo.yolo_android.ui.home_list.HomeListDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MySpotFragment: BindingFragment<FragmentMySpotBinding>(R.layout.fragment_my_spot) {

    private val viewModel: MySpotViewModel by viewModels()

    lateinit var mAdapter: MySpotPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initSwipeLayout()

        viewModel.listData.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            mAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.callbackPostId.collect {
                presentDialog(it)
            }
        }

        childFragmentManager.setFragmentResultListener(CommonDialog.REQ_RESULT_POST_ID, this) { requestKey, bundle ->
            val postId = bundle.getInt("postId")
            if (postId != -1) viewModel.deletePost(postId)
        }

    }

    private fun initAdapter() {
        mAdapter = MySpotPagingAdapter(viewModel)
        binding.rvMyspotList.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(HomeListDecoration(20.dpToPx()))
        }
    }

    private fun initSwipeLayout() {
        binding.swipeVMyspotList.setOnRefreshListener {
            mAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mAdapter.loadStateFlow.collectLatest { loadStates ->
                Log.d("error addLoadStateListener", "${loadStates}")

                binding.swipeVMyspotList.isRefreshing = loadStates.source.refresh is LoadState.Loading
                binding.llMyspotError.isVisible = loadStates.source.refresh is LoadState.Error

                val errorState = loadStates.source.refresh as? LoadState.Error
                    ?: loadStates.refresh as? LoadState.Error
                    ?: loadStates.append as? LoadState.Error
                    ?: loadStates.prepend as? LoadState.Error
                errorState?.let {
                    viewModel._toastMessage.emit(it.error.message ?: "")
                }

            }
        }

        binding.btnMyspotRetry.setOnClickListener {
            mAdapter.retry()
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

}