package com.yolo.yolo_android.ui.home_calendar.decorator

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.yolo.yolo_android.R

class NonSelectableDecorator(
    val context: Context,
    private val minCalendarDay: CalendarDay,
    private val maxCalendarDay: CalendarDay
) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.let {
            // day <= min day >= max
            return it.isBefore(minCalendarDay) || it.isAfter(maxCalendarDay)
        } ?: return false
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object :
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray)) {})
    }
}