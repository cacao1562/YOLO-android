package com.yolo.yolo_android.ui.main

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingActivity
import com.yolo.yolo_android.databinding.ActivityMainBinding
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
                    Toast.makeText(this, getString(R.string.message_please_press_again_if_you_want_finish), Toast.LENGTH_SHORT).show()
                }
            }.addTo(compositeDisposable)
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            if (it.childFragmentManager.backStackEntryCount == 0) {
                backPressedSubject.onNext(System.currentTimeMillis())
            }else {
                super.onBackPressed()
            }
        }
    }
}