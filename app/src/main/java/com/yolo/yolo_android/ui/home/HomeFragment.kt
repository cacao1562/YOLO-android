package com.yolo.yolo_android.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.takusemba.multisnaprecyclerview.MultiSnapHelper
import com.takusemba.multisnaprecyclerview.SnapGravity
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.common.extensions.ToastExt.toast
import com.yolo.yolo_android.databinding.FragmentHomeBinding
import com.yolo.yolo_android.ui.home.adapter.EventBannerAdapter
import com.yolo.yolo_android.ui.home.adapter.PlaceRankingAdapter
import com.yolo.yolo_android.ui.home.adapter.RegionViewPagerAdapter
import com.yolo.yolo_android.ui.home.adapter.RestaurantRankingAdapter
import com.yolo.yolo_android.ui.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private val regionPagerAdapter by lazy {
        RegionViewPagerAdapter(requireActivity()).apply {
            addFragment(RegionsFragment.newInstance(Regions.page1Regions))
            addFragment(RegionsFragment.newInstance(Regions.page2Regions))
            addFragment(RegionsFragment.newInstance(Regions.page3Regions))
        }
    }
    private val eventAdapter = EventBannerAdapter()
    private val eventBannerOffsetPx by lazy {
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.event_banner_page_margin)
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.event_banner_width)
        val screenMarginStart = resources.getDimensionPixelOffset(R.dimen.default_margin_24)
        val screenWidth = resources.displayMetrics.widthPixels
        screenWidth - pageMarginPx - pagerWidth - screenMarginStart
    }
    private val restaurantAdapter = RestaurantRankingAdapter()
    private val placeAdapter = PlaceRankingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = homeViewModel
        binding.rlSelectDate.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCalendarFragment()
            findNavController().navigate(action)
        }

        // 지역별 여행지 추천
        with(binding.layoutHomeRecommendByRegion) {
            vpRegion.adapter = regionPagerAdapter
            indicator.setViewPager2(vpRegion)
        }

        // 이벤트 배너
        with(binding.layoutHomeEvent) {
            vpEvent.adapter = eventAdapter
            vpEvent.offscreenPageLimit = eventAdapter.itemCount
            vpEvent.setPageTransformer { page, position ->
                page.translationX = position * -eventBannerOffsetPx
            }
        }

        // 가볼만한 인기 식당,장소 순위
        with(binding.layoutHomeVisitRanking) {
            rvRestaurantRanking.adapter = restaurantAdapter
            rvPlaceRanking.adapter = placeAdapter
            val restaurantSnapHelper = MultiSnapHelper(SnapGravity.START, 1, 50f)
            val placeSnapHelper = MultiSnapHelper(SnapGravity.START, 1, 50f)
            restaurantSnapHelper.attachToRecyclerView(rvRestaurantRanking)
            placeSnapHelper.attachToRecyclerView(rvPlaceRanking)
        }


        homeViewModel.restaurantRanking.observe(viewLifecycleOwner, {
            restaurantAdapter.setData(it)
        })


        homeViewModel.placeRanking.observe(viewLifecycleOwner, {
            placeAdapter.setData(it)
        })

        homeViewModel.toastMessage.observe(viewLifecycleOwner, {
            toast(it)
        })

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.init()
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}