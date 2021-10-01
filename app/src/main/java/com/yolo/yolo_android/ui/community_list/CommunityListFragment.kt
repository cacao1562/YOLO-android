package com.yolo.yolo_android.ui.community_list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.*
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityListBinding
import com.yolo.yolo_android.model.CallbackPostButton
import com.yolo.yolo_android.ui.dialog.CommonDialog
import com.yolo.yolo_android.ui.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    private var evnetRefresh = false
    
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
            if (it == null) return@Observer
            mAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.callbackPostBtn.collect {
                when(it) {
                    is CallbackPostButton.More -> presentDialog(it.postId)
                    is CallbackPostButton.Map -> presentMap(it)
                    is CallbackPostButton.Comment -> presentComment(it.postId)
                }
            }
        }

        mAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (evnetRefresh && itemCount == 1) {
                    binding.rvCommunityList.scrollToPosition(0)
                    evnetRefresh = false
                }
                super.onItemRangeInserted(positionStart, itemCount)
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

        viewLifecycleOwner.lifecycleScope.launch {
            mAdapter.loadStateFlow.collectLatest { loadStates ->
                Log.d("error addLoadStateListener", "${loadStates}")

                binding.swipeVCommunityList.isRefreshing = loadStates.source.refresh is LoadState.Loading
                binding.llCommunityError.isVisible = loadStates.source.refresh is LoadState.Error

                val errorState = loadStates.source.refresh as? LoadState.Error
                    ?: loadStates.refresh as? LoadState.Error
                    ?: loadStates.append as? LoadState.Error
                    ?: loadStates.prepend as? LoadState.Error
                errorState?.let {
                    viewModel._toastMessage.emit(it.error.message ?: "")
                }

            }
        }

        binding.btnCommunityRetry.setOnClickListener {
            mAdapter.retry()
        }
    }


    fun refreshAdapter() {
        evnetRefresh = true
        CoroutineScope(Dispatchers.Main).launch {
            delay(300)
            mAdapter.refresh()
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

    private fun presentComment(id: Int) {
        val action = MainFragmentDirections.actionMainFragmentToCommentListFragment(id)
        findNavController().safeNavigate(action)
    }

}