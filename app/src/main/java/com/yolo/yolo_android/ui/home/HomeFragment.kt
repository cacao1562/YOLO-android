package com.yolo.yolo_android.ui.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeBinding
import com.yolo.yolo_android.ui.main.MainFragmentDirections

class HomeFragment: BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rlSelectDate.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCalendarFragment()
            findNavController().navigate(action)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}