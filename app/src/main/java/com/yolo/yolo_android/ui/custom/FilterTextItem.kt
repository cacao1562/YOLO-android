package com.yolo.yolo_android.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.yolo.yolo_android.R

class FilterTextItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        initView(attrs)
    }

    private lateinit var selectTextView: TextView
    private lateinit var unselectTextView: TextView
    private lateinit var selectedGroup: Group

    private fun initView(attrs: AttributeSet?) {
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = li.inflate(R.layout.layout_filter_item, this, false)
        addView(v)
        selectTextView = v.findViewById<TextView>(R.id.tv_filter_item_selected)
        unselectTextView = v.findViewById<TextView>(R.id.tv_filter_item_unselected)
        selectedGroup = v.findViewById<Group>(R.id.view_group_filter_selected)
        selectedGroup.isVisible = false
        selectTextView.isVisible = false
    }

    fun setItem(title: String, isSelected: Boolean) {
        if (isSelected) {
            selectedGroup.isVisible = true
            selectTextView.text = title
        }else {
            unselectTextView.isVisible = true
            unselectTextView.text = title
        }
    }

}