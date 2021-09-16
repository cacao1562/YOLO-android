package com.yolo.yolo_android.ui.home_calendar.decorator

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.yolo.yolo_android.R

class SelectedDecorator(
    val context: Context
) : DayViewDecorator {
    val drawable = ContextCompat.getDrawable(context, R.drawable.selector_calendar)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade?) {
        drawable?.let {
            view?.setSelectionDrawable(it)
        }
    }
}