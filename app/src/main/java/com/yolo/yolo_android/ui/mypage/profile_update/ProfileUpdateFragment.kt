package com.yolo.yolo_android.ui.mypage.profile_update

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yolo.yolo_android.R
import com.yolo.yolo_android.addOnClickListener
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentProfileUpdateBinding
import com.yolo.yolo_android.model.FilterListData
import com.yolo.yolo_android.ui.dialog.CommonDialog
import com.yolo.yolo_android.ui.dialog.FilterBottomDialog
import com.yolo.yolo_android.util.MyLogger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileUpdateFragment: BindingFragment<FragmentProfileUpdateBinding>(R.layout.fragment_profile_update) {

    private val viewModel: ProfileUpdateViewModel by viewModels()

    private val args: ProfileUpdateFragmentArgs by navArgs()

    companion object {
        const val KEY_FROM_UPDATE_PROFILE = "KEY_FROM_UPDATE_PROFILE"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.setProfile(args.profile)
        binding.ivProfileUpdateBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.imageUri.observe(viewLifecycleOwner) {
            Glide.with(binding.ivProfileUpdateImg).load(it).into(binding.ivProfileUpdateImg)
        }

        childFragmentManager.setFragmentResultListener(CommonDialog.REQ_RESULT_CONFIRM, this) { requestKey, bundle ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set(KEY_FROM_UPDATE_PROFILE, true)
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateSuccess.collect {
                val dialog = when(it) {
                    "update" -> CommonDialog.newInstance(getString(R.string.profile_update), getString(R.string.alert_update_nickName))
                    "delete" -> CommonDialog.newInstance(getString(R.string.profile_update), getString(R.string.alert_delete_profile_image))
                    else -> null
                }
                dialog?.show(childFragmentManager, CommonDialog::class.java.simpleName)
            }
        }

        binding.groupProfileUpdate.addOnClickListener {
            presentDialog()
        }
    }

    private fun presentDialog() {
        val data = FilterListData("프로필 사진 변경", ProfileUpdateOptions::class.java)
        val dialog = FilterBottomDialog.newInstance(data) {
            if (it != "dismiss") {
                val type = ProfileUpdateOptions.valueOf(it)
                when(type) {
                    ProfileUpdateOptions.OPTION_01 -> viewModel.presentImagePicker(requireContext())
                    ProfileUpdateOptions.OPTION_02 -> viewModel.deleteProfileImage()
                }
            }
        }
        dialog.show(childFragmentManager, dialog.tag)
    }

    enum class ProfileUpdateOptions(val options: String) {
        OPTION_01("새 프로필 사진 선택"),
        OPTION_02("프로필 사진 삭제");

        override fun toString(): String {
            return options.toString()
        }
    }
}