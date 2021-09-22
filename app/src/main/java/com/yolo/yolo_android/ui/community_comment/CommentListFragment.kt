package com.yolo.yolo_android.ui.community_comment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.databinding.FragmentCommentListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CommentListFragment: BindingFragment<FragmentCommentListBinding>(R.layout.fragment_comment_list) {

    private val args: CommentListFragmentArgs by navArgs()

    @Inject
    lateinit var factory: CommentListViewModel.CommentyListViewModelFactory

    private val viewModel: CommentListViewModel by viewModels {
        CommentListViewModel.provideFactory(factory, args.postId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val commentListAdapter = CommentListAdapter()
        binding.rvCommentList.apply {
            adapter = commentListAdapter
            setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.commentList.observe(viewLifecycleOwner) {
                when(it) {
                    is ResultData.Success -> {
                        if (it.data.resultCode == 200) {
                            Log.d("aaa", "comment success ")
                            commentListAdapter.submitList(it.data.result)
                        }
                    }
                }
            }
        }
    }

}