package com.yolo.yolo_android.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.yolo.yolo_android.R
import com.yolo.yolo_android.YoLoApplication.Companion.naverOAuthLoginInstance
import com.yolo.yolo_android.base.BindingActivity
import com.yolo.yolo_android.common.EventObserver
import com.yolo.yolo_android.common.constants.LOGIN_TYPE
import com.yolo.yolo_android.common.constants.SOCIAL_ID
import com.yolo.yolo_android.common.extensions.ToastExt.toast
import com.yolo.yolo_android.databinding.ActivityLoginBinding
import com.yolo.yolo_android.ui.dialog.KakaoLoginTypeDialog
import com.yolo.yolo_android.ui.main.MainActivity
import com.yolo.yolo_android.ui.signup.SignupActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login),
    CoroutineScope {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        binding.vm = loginViewModel
        binding.etId.setOnFocusChangeListener { _, focus -> loginViewModel.idFocus.value = focus }

        with(loginViewModel) {
            loading.observe(this@LoginActivity, {
                if (it) {
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            })

            toastMessage.observe(this@LoginActivity, {
                toast(it)
            })

            navigateToSignup.observe(this@LoginActivity, EventObserver {
                Intent(this@LoginActivity, SignupActivity::class.java)
                    .apply {
                        putExtra(LOGIN_TYPE, it.first)
                        putExtra(SOCIAL_ID, it.second)
                    }
                    .also { startActivity(it) }
            })

            navigateToMain.observe(this@LoginActivity, {
                Intent(this@LoginActivity, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            })

            showKakaoLoginType.observe(this@LoginActivity, EventObserver {
                showKakaoLoginTypeDialog()
            })

            invalidNaverAccessToken.observe(this@LoginActivity, EventObserver {
                naverOAuthLoginInstance.startOauthLoginActivity(
                    this@LoginActivity,
                    oAuthLoginHandler
                )
            })
        }

        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
//                Log.d("xxx", "Fetch and activate succeded = $updated")
                val isVisibleLogin = mFirebaseRemoteConfig.getBoolean("isVisibleLogin")
                binding.llTestLogin.isVisible = isVisibleLogin
            }else {
//                Log.d("xxx", "Fetch fail")
            }
        }
    }

    private fun showKakaoLoginTypeDialog() {
        val dialog = KakaoLoginTypeDialog.newInstance(
            kakaoLogin = {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                    UserApiClient.instance.loginWithKakaoTalk(this, callback = kakaoLoginHandler)
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoLoginHandler)
                }
            },
            kakaoLoginWithOtherId = {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoLoginHandler)
            }
        )
        dialog.show(supportFragmentManager, "kakao_login_type_dialog")
    }

    private val kakaoLoginHandler: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (token != null && error == null) {
            loginViewModel.fetchKakaoUserInfo(token.accessToken)
        } else {
            toast((error as KakaoSdkError).msg)
        }
    }

    private val oAuthLoginHandler: OAuthLoginHandler =
        object : OAuthLoginHandler(Looper.getMainLooper()) {
            override fun run(success: Boolean) {
                if (success) {
                    loginViewModel.checkNaverAccessToken()
                } else {
                    toast(naverOAuthLoginInstance.getLastErrorDesc(this@LoginActivity))
                }
            }
        }
}