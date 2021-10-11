package com.yolo.yolo_android.ui.home_area.tab

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.yolo.yolo_android.*
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeAreaTabBinding
import com.yolo.yolo_android.model.FilterListData
import com.yolo.yolo_android.model.Region
import com.yolo.yolo_android.ui.dialog.FilterBottomDialog

class HomeAreaTabFragment: BindingFragment<FragmentHomeAreaTabBinding>(R.layout.fragment_home_area_tab) {

    private val args: HomeAreaTabFragmentArgs by navArgs()

    private var mSelectedType: AreaListOptions = AreaListOptions.OPTION_01

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.region?.let {
            binding.tvHomeTabTitle.text = it.areaName

            mSelectedType = AreaListOptions.from(it.areaName)
            binding.vp2HomeTab.adapter = HomeAreaListPagerAdapter(it.areaCode.toInt(), childFragmentManager, lifecycle)
            TabLayoutMediator(binding.tlHomeTab, binding.vp2HomeTab) { tab, position ->
                tab.text = TabTitle[position]
            }.attach()
            binding.tlHomeTab.setMargin(0,0, 12.dpToPx(),0)
        }

        binding.ivHomeTabBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvHomeTabTitle.setOnClickListener {
            presentAreaBottomDialog()
        }
        binding.ivHomeTabTitleDropdown.setOnClickListener {
            presentAreaBottomDialog()
        }

    }

    private fun presentAreaBottomDialog() {
        rotateFilterArrow(false, binding.ivHomeTabTitleDropdown)

        val data = FilterListData("지역 선택", AreaListOptions::class.java, mSelectedType)
        val dialog = FilterBottomDialog.newInstance(data) {
            if (it == "dismiss") {
                rotateFilterArrow(true, binding.ivHomeTabTitleDropdown)
            }else {
                mSelectedType = AreaListOptions.valueOf(it)
                binding.tvHomeTabTitle.text = mSelectedType.areaName

                val action = HomeAreaTabFragmentDirections.actionHomeTabFragmentSelf()
                action.region = Region(-1, mSelectedType.areaCode.toString(), mSelectedType.areaName)
                findNavController().safeNavigate(action)

            }
        }
        dialog.show(childFragmentManager, dialog.tag)
    }

}

enum class AreaListOptions(val areaName: String, val areaCode: Int) {
    OPTION_01("서울", 1),
    OPTION_02("인천", 2),
    OPTION_03("대전", 3),
    OPTION_04("대구", 4),
    OPTION_05("광주", 5),
    OPTION_06("부산", 6),
    OPTION_07("울산", 7),
    OPTION_08("세종", 8),
    OPTION_09("경기도", 31),
    OPTION_10("강원도", 32),
    OPTION_11("충청북도", 33),
    OPTION_12("충청남도", 34),
    OPTION_13("경상북도", 35),
    OPTION_14("경상남도", 36),
    OPTION_15("전라북도", 37),
    OPTION_16("전라남도", 38),
    OPTION_17("제주도", 39);

    override fun toString(): String {
        return areaName.toString()
    }

    companion object {
        fun from(type: String?): AreaListOptions = values().find { it.areaName == type } ?: OPTION_01
    }
}