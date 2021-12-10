package com.yolo.yolo_android.ui.community

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.yolo.yolo_android.CommunitySort
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityBinding
import com.yolo.yolo_android.safeNavigate
import com.yolo.yolo_android.ui.community_list.CommunityListFragment
import com.yolo.yolo_android.ui.community_upload.CommunityUploadFragment
import com.yolo.yolo_android.ui.main.MainFragmentDirections

class CommunityFragment: BindingFragment<FragmentCommunityBinding>(R.layout.fragment_community) {

    companion object {
        fun newInstance() = CommunityFragment()
    }

    private val tabTitle = arrayOf("인기 공유스팟", "최신 공유스팟")

    private val fragments = listOf(
            CommunityListFragment.newInstance(CommunitySort.ByLiked),
            CommunityListFragment.newInstance(CommunitySort.ByLatest))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CommunityTabPagerAdapter(requireActivity())
        binding.vp2Community.adapter = adapter
        adapter.setFragment(fragments)
        TabLayoutMediator(binding.tlCommunityTab, binding.vp2Community) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
        binding.vp2Community.isUserInputEnabled = false
//        binding.vp2Community.isSaveEnabled = false
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>(CommunityUploadFragment.KEY_FROM_UPLOAD_SUCCESS)?.observe(viewLifecycleOwner) {
                if (it) {
                    binding.tlCommunityTab.getTabAt(1)?.select()
                    fragments[1].refreshAdapter()

                    findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
                        CommunityUploadFragment.KEY_FROM_UPLOAD_SUCCESS
                    )
                }
            }

        binding.ivCreatePost.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCommunityUploadFragment()
            findNavController().safeNavigate(action)
        }
    }
}