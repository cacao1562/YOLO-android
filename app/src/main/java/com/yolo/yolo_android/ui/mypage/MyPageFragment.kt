package com.yolo.yolo_android.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yolo.yolo_android.R
import com.yolo.yolo_android.USER_GUIDE_URL
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentMypageBinding
import com.yolo.yolo_android.safeNavigate
import com.yolo.yolo_android.ui.dialog.ConfirmCancelDialog
import com.yolo.yolo_android.ui.main.MainFragmentDirections
import com.yolo.yolo_android.ui.mypage.profile_update.ProfileUpdateFragment
import com.yolo.yolo_android.utils.FileDownloader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment: BindingFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private val viewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.cvMypageProfile.setOnClickListener {
            presentProfileUpdate()
        }
        binding.llMypageProfileName.setOnClickListener {
            presentProfileUpdate()
        }
        binding.ivMypageSetting.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSettingFragment()
            findNavController().safeNavigate(action)
        }
        binding.tvMypageMySpot.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToMySpotFragment()
            findNavController().safeNavigate(action)
        }
        binding.tvMypageServiceCenter.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToServiceCenterFragment()
            findNavController().safeNavigate(action)
        }
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>(ProfileUpdateFragment.KEY_FROM_UPDATE_PROFILE)?.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.callGetMyProfile()
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(ProfileUpdateFragment.KEY_FROM_UPDATE_PROFILE)
            }
        }
        binding.llNotice.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToWebFragment("viridian-monarch-554.notion.site/fc2796ce4a284b6a978d5005f7316aca")
            findNavController().safeNavigate(action)
        }
        binding.llUserGuide.setOnClickListener {
            val dialog = ConfirmCancelDialog
                .newInstance(
                    msg = getString(R.string.settings_message_download_user_guide),
                    confirm = {
                        FileDownloader(requireContext(), fileName = getString(R.string.setting_user_guide), fullPath = USER_GUIDE_URL).download()
                    }
                )
            dialog.show(childFragmentManager, ConfirmCancelDialog::class.java.simpleName)
        }
    }

    fun presentProfileUpdate() {
        viewModel.myProfile.value?.let {
            val action = MainFragmentDirections.actionMainFragmentToProfileUpdateFragment(it)
            findNavController().safeNavigate(action)
        }
    }
}