package com.yolo.yolo_android.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.yolo.yolo_android.R
import com.yolo.yolo_android.databinding.DialogConfirmCancelBinding

class ConfirmCancelDialog : DialogFragment() {
    private lateinit var binding: DialogConfirmCancelBinding
    private var confirm: (() -> Unit)? = null
    private var cancel: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfirmCancelBinding.inflate(layoutInflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnDialogConfirm.setOnClickListener {
            confirm?.let { it() }
            dismissAllowingStateLoss()
        }
        binding.btnDialogCancel.setOnClickListener {
            cancel?.let { it() }
            dismissAllowingStateLoss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(KEY_TITLE)
        binding.tvDialogTitle.text = if (title.isNullOrEmpty()) {
            getString(R.string.alert)
        } else {
            title
        }
        binding.tvDialogMsg.text = arguments?.getString(KEY_MSG)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setLayout(width, height)
        }
    }

    companion object {
        fun newInstance(
            title: String = "",
            msg: String,
            confirm: (() -> Unit)? = null,
            cancel: (() -> Unit)? = null
        ): ConfirmCancelDialog {
            val fragment = ConfirmCancelDialog()
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_MSG, msg)
            fragment.confirm = confirm
            fragment.cancel = cancel
            fragment.arguments = args
            return fragment
        }

        const val KEY_TITLE = "KEY_TITLE"
        const val KEY_MSG = "KEY_MSG"
    }
}