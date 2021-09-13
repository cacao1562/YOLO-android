package com.yolo.yolo_android.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.yolo.yolo_android.databinding.DialogKakaoLoginTypeBinding
import com.yolo.yolo_android.dpToPx
import com.yolo.yolo_android.toDp

class KakaoLoginTypeDialog : DialogFragment() {
    private var kakaoLogin: (() -> Unit)? = null
    private var kakaoLoginWithOtherId: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogKakaoLoginTypeBinding.inflate(inflater, container, false)
        binding.llLogin.setOnClickListener {
            kakaoLogin?.let { it() }
            dismiss()
        }

        binding.llLoginWithOther.setOnClickListener {
            kakaoLoginWithOtherId?.let { it() }
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.let {
            it.window?.let { window ->
                with(window) {
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setGravity(Gravity.BOTTOM)
                    setLayout(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    val layoutParams = attributes
                    layoutParams.y = 35.toDp().toInt().dpToPx()
                    attributes = layoutParams
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    companion object {
        fun newInstance(
            kakaoLogin: (() -> Unit)? = null,
            kakaoLoginWithOtherId: (() -> Unit)? = null
        ): KakaoLoginTypeDialog {
            val dialog = KakaoLoginTypeDialog()
            dialog.kakaoLogin = kakaoLogin
            dialog.kakaoLoginWithOtherId = kakaoLoginWithOtherId
            return dialog
        }
    }
}