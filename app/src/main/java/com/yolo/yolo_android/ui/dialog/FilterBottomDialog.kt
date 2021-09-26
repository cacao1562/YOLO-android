package com.yolo.yolo_android.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yolo.yolo_android.databinding.DialogFilterBottomBinding
import com.yolo.yolo_android.dpToPx
import com.yolo.yolo_android.model.FilterListData
import com.yolo.yolo_android.ui.custom.FilterTextItem


class FilterBottomDialog: BottomSheetDialogFragment() {

    companion object {

        const val KEY_FilterListData = "KEY_FilterListData"
        lateinit var clickResult: (String) -> Unit
        fun <T: Enum<T>> newInstance(data: FilterListData<T>, callback: (String) -> Unit): FilterBottomDialog{
            val fragment = FilterBottomDialog()
            val bundle = Bundle()
            bundle.putParcelable(KEY_FilterListData, data)
            fragment.arguments = bundle
            clickResult = callback
            return fragment
        }
    }

    private lateinit var binding: DialogFilterBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFilterBottomBinding.inflate(layoutInflater)
        val data = arguments?.getParcelable<FilterListData<*>>(KEY_FilterListData)
        binding.tvFilterDialogTitle.text = data?.title
        data?.option?.enumConstants?.forEach { enum ->
            val item = FilterTextItem(binding.root.context)
            item.setItem(enum.toString(), enum == data.isSelected)
            item.setOnClickListener {
                clickResult.invoke(enum.name)
                dismissAllowingStateLoss()
            }
            binding.llFilterDialog.addView(item)
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
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.yolo.yolo_android.R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet!!.layoutParams
        val data = arguments?.getParcelable<FilterListData<*>>(KEY_FilterListData)
        var size = (data?.option?.enumConstants?.size ?: 4)
        val defaultH = if (size > 4) {
            size = 4
            72.dpToPx() + 26.dpToPx()
        }else {
            72.dpToPx()
        }
        layoutParams.height = defaultH + (52.dpToPx() * size)  // getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 47 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}