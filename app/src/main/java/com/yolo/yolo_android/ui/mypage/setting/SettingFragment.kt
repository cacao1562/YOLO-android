package com.yolo.yolo_android.ui.mypage.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.kakao.sdk.user.UserApiClient
import com.yolo.yolo_android.*
import com.yolo.yolo_android.YoLoApplication.Companion.naverOAuthLoginInstance
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.common.EventObserver
import com.yolo.yolo_android.common.extensions.ToastExt.toast
import com.yolo.yolo_android.common.extensions.ViewExt.openExternalWebView
import com.yolo.yolo_android.databinding.FragmentSettingBinding
import com.yolo.yolo_android.ui.dialog.ConfirmCancelDialog
import com.yolo.yolo_android.ui.login.LoginActivity
import com.yolo.yolo_android.ui.main.MainFragmentDirections
import com.yolo.yolo_android.utils.MyLogger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BindingFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    private val viewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.llSettingNotice.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToNoticeFragment2()
            findNavController().safeNavigate(action)
        }
        binding.tvSettingVersion.text = BuildConfig.VERSION_NAME
        binding.llSettingLibrary.setOnClickListener {
            startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
        }
        binding.llSettingPolicy.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToWebFragment(YOLO_POLICY_URL, getString(R.string.setting_policy))
            findNavController().safeNavigate(action)
        }
        binding.llSettingSuggestionGuide.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToWebFragment(YOLO_SUGGESTION_GUIDE_URL, getString(R.string.setting_suggestion_guide))
            findNavController().safeNavigate(action)
        }
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

        binding.switchPushConfig.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changePushConfig(isChecked)
        }

        viewModel.pushConfig?.asLiveData()?.observe(viewLifecycleOwner) {
            binding.switchPushConfig.isChecked = it
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