package com.yolo.yolo_android.ui.home_calendar.decorator

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.threeten.bp.DayOfWeek

class SaturdayDecorator : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        val weekDay = day?.date?.dayOfWeek
        return weekDay == DayOfWeek.SATURDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object : ForegroundColorSpan(Color.BLUE) {})
    }
}