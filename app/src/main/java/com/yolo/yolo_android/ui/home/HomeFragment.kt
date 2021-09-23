package com.yolo.yolo_android.ui.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeBinding
import com.yolo.yolo_android.ui.home.adapter.EventBannerAdapter
import com.yolo.yolo_android.ui.home.adapter.RegionViewPagerAdapter
import com.yolo.yolo_android.ui.main.MainFragmentDirections

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rlSelectDate.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCalendarFragment()
            findNavController().navigate(action)
        }

        val regionPagerAdapter = RegionViewPagerAdapter(requireActivity()).apply {
            addFragment(RegionsFragment.newInstance(Regions.page1Regions))
            addFragment(RegionsFragment.newInstance(Regions.page2Regions))
            addFragment(RegionsFragment.newInstance(Regions.page3Regions))
        }

        with(binding.layoutHomeRecommendByRegion) {
            vpRegion.adapter = regionPagerAdapter
            indicator.setViewPager2(vpRegion)
        }

        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.event_banner_page_margin)
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.event_banner_width)
        val screenWidth = resources.displayMetrics.widthPixels
        val screenMarginStart = resources.getDimensionPixelOffset(R.dimen.default_margin_24)
        val offsetPx = screenWidth - pageMarginPx - pagerWidth - screenMarginStart

        val eventAdapter = EventBannerAdapter()
        with(binding.layoutHomeEvent) {
            vpEvent.adapter = eventAdapter
            vpEvent.offscreenPageLimit = eventAdapter.itemCount
            vpEvent.setPageTransformer { page, position ->
                page.translationX = position * -offsetPx
            }
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}