package com.yolo.yolo_android.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.yolo.yolo_android.databinding.DialogCommonBinding

class CommonDialog: DialogFragment() {

    companion object {
        const val KEY_TITLE = "KEY_TITLE"
        const val KEY_MSG = "KEY_MSG"
        fun newInstance(title: String, msg: String): CommonDialog {
            val fragment = CommonDialog()
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_MSG, msg)
            fragment.arguments = args
            return fragment
        }
    }

    private val args: CommonDialogArgs by navArgs()

    private lateinit var binding: DialogCommonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCommonBinding.inflate(layoutInflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnDialogConfirm.setOnClickListener {
            dismissAllowingStateLoss()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDialogTitle.text = args.title
        binding.tvDialogMsg.text = args.msg
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