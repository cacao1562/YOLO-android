package com.yolo.yolo_android.ui.mypage.notice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentNoticeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeFragment: BindingFragment<FragmentNoticeBinding>(R.layout.fragment_notice) {

    private val viewModel: NoticeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        val noticeAdapter = NoticeAdapter()
        binding.rvNotice.apply {
            adapter = noticeAdapter
            setHasFixedSize(true)
        }
        viewModel.data.observe(viewLifecycleOwner) {
            noticeAdapter.submitList(it)
        }
    }
}