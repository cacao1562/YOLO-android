package com.yolo.yolo_android.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentMypageBinding
import com.yolo.yolo_android.safeNavigate
import com.yolo.yolo_android.ui.main.MainFragmentDirections
import com.yolo.yolo_android.ui.mypage.profile_update.ProfileUpdateFragment
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
        /**
         * 설정
         */
        binding.ivMypageSetting.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSettingFragment()
            findNavController().safeNavigate(action)
        }
        /**
         * 찜 목록
         */
        binding.tvMypageBookMark.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToBookMarkFragment()
            findNavController().safeNavigate(action)
        }
        /**
         * 마이페이지
         */
        binding.tvMypageMySpot.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToMySpotFragment()
            findNavController().safeNavigate(action)
        }
        /**
         * 고객센터
         */
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
    }

    fun presentProfileUpdate() {
        viewModel.myProfile.value?.let {
            val action = MainFragmentDirections.actionMainFragmentToProfileUpdateFragment(it)
            findNavController().safeNavigate(action)
        }
    }
}