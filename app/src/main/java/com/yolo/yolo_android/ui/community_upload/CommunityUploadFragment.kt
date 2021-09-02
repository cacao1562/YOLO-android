package com.yolo.yolo_android.ui.community_upload

import android.Manifest
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.*
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityUploadBinding
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
        if (isPermissionGranted(requireContext(), Manifest.permission.ACCESS_MEDIA_LOCATION)) {
            requestPermission(requireActivity(), Manifest.permission.ACCESS_MEDIA_LOCATION)
        }
        binding.ivImagePicker.setOnClickListener {
            TedImagePicker.with(requireContext()).startMultiImage {
                it.forEach {
                    try {
                        val path = uri2path(requireContext(), it) ?: ""
                        val exif = ExifInterface(path)
                        exif.latLong?.forEach {
                            Log.d("image gps", "value=$it")
                        }
                    }catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
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