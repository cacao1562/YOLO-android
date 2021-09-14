package com.yolo.yolo_android.ui.home_calendar.decorator

import android.content.Context
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.yolo.yolo_android.R

class SelectedDecorator(
    val context: Context,
    val calendarDay: CalendarDay
) : DayViewDecorator {
    val drawable = ContextCompat.getDrawable(context, R.drawable.bg_calendar_selected)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.let {
            return (it == calendarDay)
        } ?: return false
    }

    override fun decorate(view: DayViewFacade?) {
        drawable?.let {
            view?.setSelectionDrawable(it)
        }
    }
}