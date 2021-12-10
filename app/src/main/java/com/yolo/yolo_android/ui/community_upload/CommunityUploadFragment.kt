package com.yolo.yolo_android.ui.community_upload

import android.Manifest
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityUploadBinding
import com.yolo.yolo_android.model.Document
import com.yolo.yolo_android.presentAppSetting
import com.yolo.yolo_android.ui.dialog.CommonDialog
import com.yolo.yolo_android.ui.place_list.PlaceListFragment
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommunityUploadFragment: BindingFragment<FragmentCommunityUploadBinding>(R.layout.fragment_community_upload) {

    companion object {
        const val KEY_FROM_UPLOAD_SUCCESS = "KEY_FROM_UPLOAD_SUCCESS"
    }
    private val viewModel: CommunityUploadViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: MutableMap<String, Boolean> ->

            val deniedList: List<String> = result.filter {
                !it.value
            }.map {
                it.key
            }

            when {
                deniedList.isNotEmpty() -> {
                    val map = deniedList.groupBy { permission ->
                        if (shouldShowRequestPermissionRationale(permission)) "DENIED" else "EXPLAINED"
                    }
                    map["DENIED"]?.let {
                        // request denied , request again
                        AlertDialog.Builder(requireContext())
                            .setTitle("필수 권한 허용 안내")
                            .setMessage("\n아래와 같은 이유로 권한 허용이 필요합니다.\n • 이미지 업로드")
                            .setPositiveButton("권한재요청") { dialog, which ->
                                requestNewPermission(deniedList.toTypedArray(), mGrantedBlock)
                            }
                            .setNegativeButton("닫기", null)
                            .create()
                            .show()
                    }
                    map["EXPLAINED"]?.let {
                        //request denied ,send to settings
                        AlertDialog.Builder(requireContext())
                            .setTitle("필수 권한 허용 안내")
                            .setMessage("\n아래와 같은 이유로 권한 허용이 필요합니다.\n • 이미지 업로드")
                            .setPositiveButton("설정") { dialog, which ->
                                presentAppSetting(requireContext())
                            }
                            .setNegativeButton("닫기", null)
                            .create()
                            .show()
                    }
                }
                else -> {
                    mGrantedBlock.invoke()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel

        binding.ivImagePicker.setOnClickListener {

            requestNewPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                                         Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                         Manifest.permission.CAMERA)) {
                presentImagePicker()
            }
        }
        setFragmentResultListener(PlaceListFragment.REQ_SELECTED_PLACE) { requestKey, bundle ->
            val result = bundle.getParcelable<Document>("data")
            Log.d("aaa", "Document=$result")
            result?.let {
                viewModel.setDocument(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect {
                Log.d("aaa", "events=$it")
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    KEY_FROM_UPLOAD_SUCCESS, true)
                findNavController().popBackStack()
            }
        }
        return root
    }
    private var mGrantedBlock : () -> Unit = { }

    fun requestNewPermission(
        permissions: Array<String>,
        grantedBlock: (() -> Unit) = { }
    ) {
        mGrantedBlock = grantedBlock
        requestPermissionLauncher.launch(permissions)
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