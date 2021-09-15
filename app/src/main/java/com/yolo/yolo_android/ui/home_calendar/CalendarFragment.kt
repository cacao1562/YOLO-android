package com.yolo.yolo_android.ui.home_calendar

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCalendarBinding
import com.yolo.yolo_android.ui.home_calendar.decorator.*
import org.threeten.bp.LocalDate

class CalendarFragment : BindingFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivClose.setOnClickListener { findNavController().popBackStack() }

        initCalendar()
    }

    private fun initCalendar() {
        binding.calendarView.apply {
            addDecorators(
                SelectedDecorator(requireContext()),
                SaturdayDecorator(),
                SundayDecorator(),
                NonSelectableDecorator(
                    requireContext(),
                    CalendarDay.from(minLocalDate()),
                    CalendarDay.from(maxLocalDate())
                )
            )
            state().edit()
                .setMinimumDate(CalendarDay.from(minLocalDate()))
                .setMaximumDate(CalendarDay.from(maxLocalDate()))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

            setOnDateChangedListener(onDateSelectedListener)

        }
    }

    private val onDateSelectedListener =
        OnDateSelectedListener { _, calendarDay, _ -> }

    companion object {
        fun newInstance(): CalendarFragment {
            val args = Bundle()
            val fragment = CalendarFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

fun minLocalDate(): LocalDate = LocalDate.now()
fun maxLocalDate(): LocalDate = LocalDate.now().plusDays(30)