package com.yolo.yolo_android.ui.mypage.book_mark

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentBookMarkBinding
import com.yolo.yolo_android.dpToPx
import com.yolo.yolo_android.ui.home_area.list.HomeListDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookMarkFragment: BindingFragment<FragmentBookMarkBinding>(R.layout.fragment_book_mark) {

    private val viewModel: BookMarkViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookMarkAdapter = BookMarkAdapter(viewModel)
        binding.rvBookMark.apply {
            adapter = bookMarkAdapter
            setHasFixedSize(true)
            addItemDecoration(HomeListDecoration(20.dpToPx()))
        }

        viewModel.listData.observe(viewLifecycleOwner) {
            bookMarkAdapter.submitList(it)
        }

    }

}