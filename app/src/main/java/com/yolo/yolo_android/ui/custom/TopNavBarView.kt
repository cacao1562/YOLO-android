package com.yolo.yolo_android.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.yolo.yolo_android.R

class TopNavBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    init {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = li.inflate(R.layout.layout_navigation_bar, this, false)
        addView(v)
        val backBtn = findViewById<ImageView>(R.id.iv_layout_nav_back)
        val titleView = findViewById<TextView>(R.id.tv_layout_nav_bar)
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TopNavBarView)
        try {
            val visibleBack = ta.getBoolean(R.styleable.TopNavBarView_visible_back,false)
            val title = ta.getString(R.styleable.TopNavBarView_title_text)
            backBtn.isVisible = visibleBack
            titleView.text = title
        } finally {
            ta.recycle()
        }
    }
}