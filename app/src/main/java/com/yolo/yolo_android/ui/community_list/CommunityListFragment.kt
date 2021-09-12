package com.yolo.yolo_android.ui.community_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.yolo.yolo_android.DialogButtonType
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityListBinding
import com.yolo.yolo_android.ui.dialog.CommonDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommunityListFragment: BindingFragment<FragmentCommunityListBinding>(R.layout.fragment_community_list) {

    companion object {
        fun newInstance() = CommunityListFragment()
    }

    private var job: Job? = null

    private val listViewModel: CommunityListViewModel by viewModels()
    lateinit var mAdapter: CommunityListPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initSwipeLayout()
        getPosts()
        childFragmentManager.setFragmentResultListener(CommonDialog.REQ_RESULT_POST_ID, this) { requestKey, bundle ->
            val postId = bundle.getInt("postId")
            if (postId != -1) listViewModel.deletePost()
        }
        listViewModel.deletePostId.observe(viewLifecycleOwner, Observer {
            if (it != -1) presentDialog(it)
        })
    }

    private fun initAdapter() {
        mAdapter = CommunityListPagingAdapter(listViewModel)
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

    private fun getPosts() {
        job?.cancel()
        job = lifecycleScope.launch {
            listViewModel.getPosts().collectLatest {
                mAdapter.submitData(it)
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

}