package com.yolo.yolo_android.ui.mypage.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.kakao.sdk.user.UserApiClient
import com.yolo.yolo_android.BuildConfig
import com.yolo.yolo_android.R
import com.yolo.yolo_android.YoLoApplication.Companion.naverOAuthLoginInstance
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.common.EventObserver
import com.yolo.yolo_android.common.extensions.ToastExt.toast
import com.yolo.yolo_android.databinding.FragmentSettingBinding
import com.yolo.yolo_android.ui.dialog.ConfirmCancelDialog
import com.yolo.yolo_android.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BindingFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    private val viewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSettingVersion.text = BuildConfig.VERSION_NAME
        binding.llSettingNotice.setOnClickListener { }
        binding.llSettingPolicy.setOnClickListener { }
        binding.llSettingUserGuide.setOnClickListener { }
        binding.llSettingSuggestionGuide.setOnClickListener { }
        binding.llSettingUserExpire.setOnClickListener {
            val dialog = ConfirmCancelDialog
                .newInstance(
                    msg = getString(R.string.setting_message_delete_account),
                    confirm = {
                        viewModel.deleteAccount()
                    }
                )
            dialog.show(childFragmentManager, ConfirmCancelDialog::class.java.simpleName)
        }
        binding.llSettingLogout.setOnClickListener {
            val dialog = ConfirmCancelDialog
                .newInstance(
                    msg = getString(R.string.setting_message_logout),
                    confirm = {
                        viewModel.logout()
                    }
                )
            dialog.show(childFragmentManager, ConfirmCancelDialog::class.java.simpleName)
        }

        viewModel.kakaoLogout.observe(viewLifecycleOwner, EventObserver {
            UserApiClient.instance.logout {
                if (it != null) toast("${it.message}")
            }
        })

        viewModel.naverLogout.observe(viewLifecycleOwner, EventObserver {
            naverOAuthLoginInstance.logout(requireContext())
        })

        viewModel.navigateToLoginPage.observe(viewLifecycleOwner, EventObserver {
            Intent(requireContext(), LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
        })
    }

}