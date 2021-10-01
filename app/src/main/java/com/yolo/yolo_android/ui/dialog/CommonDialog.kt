package com.yolo.yolo_android.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.yolo.yolo_android.DialogButtonType
import com.yolo.yolo_android.databinding.DialogCommonBinding
import com.yolo.yolo_android.util.MyLogger

class CommonDialog: DialogFragment() {

    companion object {
        const val KEY_TITLE = "KEY_TITLE"
        const val KEY_MSG = "KEY_MSG"
        const val KEY_TYPE = "KEY_TYPE"
        const val KEY_POST_ID = "KEY_POST_ID"
        const val REQ_RESULT_CONFIRM = "REQ_RESULT_CONFIRM"
        const val REQ_RESULT_POST_ID = "REQ_RESULT_POST_ID"

        fun newInstance(title: String,
                        msg: String,
                        btnType: DialogButtonType = DialogButtonType.Cancel,
                        postId: Int = -1): CommonDialog {
            val fragment = CommonDialog()
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_MSG, msg)
            args.putSerializable(KEY_TYPE, btnType)
            args.putInt(KEY_POST_ID, postId)
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var binding: DialogCommonBinding

    private var mPostId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCommonBinding.inflate(layoutInflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnDialogConfirm.setOnClickListener {
            setFragmentResult(REQ_RESULT_CONFIRM, bundleOf())
            dismissAllowingStateLoss()
        }
        binding.btnDialogCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.btnDialogConfirm2.setOnClickListener {
            setFragmentResult(REQ_RESULT_POST_ID, bundleOf("postId" to mPostId))
            dismissAllowingStateLoss()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDialogTitle.text = arguments?.getString(KEY_TITLE)
        binding.tvDialogMsg.text = arguments?.getString(KEY_MSG)
        binding.type = arguments?.getSerializable(KEY_TYPE) as DialogButtonType
        mPostId = arguments?.getInt(KEY_POST_ID) ?: -1
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setLayout(width, height)
        }
    }
}