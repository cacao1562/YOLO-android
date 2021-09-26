package com.yolo.yolo_android.ui.community_upload

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityUploadBinding
import com.yolo.yolo_android.model.Document
import com.yolo.yolo_android.ui.dialog.CommonDialog
import com.yolo.yolo_android.ui.place_list.PlaceListFragment
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommunityUploadFragment: BindingFragment<FragmentCommunityUploadBinding>(R.layout.fragment_community_upload) {

    companion object {
        fun newInstance() = CommunityUploadFragment()
    }

    private val viewModel: CommunityUploadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel

        binding.ivImagePicker.setOnClickListener {
            presentImagePicker()
        }
        setFragmentResultListener(PlaceListFragment.REQ_SELECTED_PLACE) { requestKey, bundle ->
            val result = bundle.getParcelable<Document>("data")
            Log.d("aaa", "Document=$result")
            result?.let {
                viewModel.setDocument(it)
            }
        }

        lifecycleScope.launch {
            viewModel.events.collect {
                Log.d("aaa", "events=$it")
                findNavController().popBackStack()
            }
        }
        return root
    }

    private fun presentImagePicker() {
        TedImagePicker.with(requireContext()).startMultiImage {
            if (it.size > 3) {
                val dialog = CommonDialog.newInstance(getString(R.string.alert), getString(R.string.alert_msg_picture_limit))
                dialog.show(childFragmentManager, CommonDialog::class.java.simpleName)
            }
            viewModel.setUri(it)
        }
    }
}

class ImageSelectedDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = space
    }

}