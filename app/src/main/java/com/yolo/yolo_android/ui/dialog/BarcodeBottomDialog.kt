package com.yolo.yolo_android.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.yolo.yolo_android.databinding.DialogCouponBottomBinding
import com.yolo.yolo_android.getWindowHeight


class BarcodeBottomDialog: BottomSheetDialogFragment(){

    companion object {
        const val KEY_CODE = "KEY_CODE"
        fun newInstance(code: String): BarcodeBottomDialog {
            val dialog = BarcodeBottomDialog()
            dialog.apply {
                arguments = bundleOf(KEY_CODE to code)
            }
            return dialog
        }
    }

    private lateinit var binding: DialogCouponBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCouponBottomBinding.inflate(layoutInflater)
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(arguments?.getString(KEY_CODE), BarcodeFormat.CODE_128, 750, 150)
            binding.ivDialogCouponBarcode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface: DialogInterface? ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog?
            setupRatio(bottomSheetDialog!!)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.yolo.yolo_android.R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height =  getWindowHeight(requireContext()) * 90 / 100
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }


}