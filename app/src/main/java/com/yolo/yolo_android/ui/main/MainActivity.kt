package com.yolo.yolo_android.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingActivity
import com.yolo.yolo_android.common.constants.FCM_ACTION
import com.yolo.yolo_android.databinding.ActivityMainBinding
import com.yolo.yolo_android.fcm.FcmType
import com.yolo.yolo_android.safeNavigate
import com.yolo.yolo_android.utils.MyLogger
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val compositeDisposable = CompositeDisposable()
    private val backPressedSubject = BehaviorSubject.createDefault(0L)

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

        backPressedSubject.buffer(2, 1)
            .map { it[0] to it[1] }
            .subscribe {
                if (it.second - it.first < 2000L) {
                    super.onBackPressed()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.message_please_press_again_if_you_want_finish),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addTo(compositeDisposable)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val fcmType = FcmType.pickByAction(intent?.extras?.getString(FCM_ACTION))
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        when (fcmType) {
            FcmType.Like, FcmType.Comment -> {
                val action = MainFragmentDirections.actionMainFragmentToCommunityFragment()
                navController?.safeNavigate(action)
            }

            FcmType.Magazine -> {
                val action = MainFragmentDirections.actionMainFragmentToMagazineFragment()
                navController?.safeNavigate(action)
            }
            else -> {
                return
            }
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            if (it.childFragmentManager.backStackEntryCount == 0) {
                backPressedSubject.onNext(System.currentTimeMillis())
            } else {
                super.onBackPressed()
            }
        }
    }
}