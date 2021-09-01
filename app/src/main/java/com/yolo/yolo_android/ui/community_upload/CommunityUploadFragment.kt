package com.yolo.yolo_android.ui.community_upload

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityUploadBinding
import com.yolo.yolo_android.dpToPx
import gun0912.tedimagepicker.builder.TedImagePicker

class CommunityUploadFragment: BindingFragment<FragmentCommunityUploadBinding>(R.layout.fragment_community_upload) {

    companion object {
        fun newInstance() = CommunityUploadFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageSelectedAdapter = ImageSelectedAdapter()
        binding.rvImageSelected.apply {
            adapter = imageSelectedAdapter
            setHasFixedSize(true)
            addItemDecoration(ImageSelectedDecoration(10.dpToPx()))
        }
        binding.ivImagePicker.setOnClickListener {
            TedImagePicker.with(requireContext()).startMultiImage {
                imageSelectedAdapter.setData(it)
            }
        }
    }
}

class ImageSelectedDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = space
    }

}