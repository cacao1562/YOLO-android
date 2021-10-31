package com.yolo.yolo_android.ui.home_detail

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.common.extensions.ToastExt.toast
import com.yolo.yolo_android.databinding.FragmentHomeDetailBinding
import com.yolo.yolo_android.setUnderLineText
import com.yolo.yolo_android.ui.home_detail.map.HomeDetailMapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class HomeDetailFragment: BindingFragment<FragmentHomeDetailBinding>(R.layout.fragment_home_detail), HomeDetailMapFragment.OnMapTouchListener {

    private val args: HomeDetailFragmentArgs by navArgs()

    @Inject
    lateinit var factory: HomeDetailViewModel.HomeDetailViewModelFactory

    private val viewModel: HomeDetailViewModel by viewModels {
        HomeDetailViewModel.provideFactory(factory, args.contentId, args.contentTypeId)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        initAppbar()
        initTopImageViewPager()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventResponse.collect { event ->
                if(event) {
                    initMoreBtn()
                    initPlaceTab()
                    initTextLink()
                }
            }
        }

        binding.ivDetailHeaderBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onTouch() {
        binding.nSvHomeDetail.requestDisallowInterceptTouchEvent(true)
    }

    private fun initAppbar() {
        binding.detailAppbarLayout.addOnOffsetChangedListener(object :
            AppBarLayout.OnOffsetChangedListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
                Log.d("aaa", " p1 = $p1")
                if (binding.detailAppbarLayout.totalScrollRange == 0 || p1 == 0) {
                    activity?.window?.statusBarColor = Color.argb(0, 0, 0, 0)
                    binding.detailHeader.setBackgroundColor(Color.argb(0, 0, 0, 0))
                    val white = Color.argb(255, 255, 255, 255)
                    binding.ivDetailHeaderBack.setColorFilter(white)
                    return
                }
                val ratio = p1.toFloat() / binding.detailAppbarLayout.totalScrollRange.toFloat()

                val rgb = (255 * abs(ratio)).toInt()
                val iconRgb = abs((255 * abs(ratio)) - 255).toInt()

                activity?.window?.statusBarColor = Color.argb(rgb, 255, 255, 255)
                binding.detailHeader.setBackgroundColor(Color.argb(rgb, 255, 255, 255))

                val tintColor = Color.argb(iconRgb, iconRgb, iconRgb, iconRgb)
                binding.ivDetailHeaderBack.setColorFilter(tintColor)

                if (abs(ratio) > 0.15) {
                    activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    activity?.window?.decorView?.systemUiVisibility = 0
                }

                binding.tvDetailHeaderTitle.isVisible = abs(ratio) >= 1.0

            }

        })
    }

    private fun initMoreBtn() {
        MainScope().launch {
            delay(300)
            try {
                val lineCount = binding.tvHomeDetailContent.layout.lineCount
                if (lineCount > 0) {
                    if (binding.tvHomeDetailContent.layout.getEllipsisCount(lineCount - 1) > 0) {
                        binding.tvHomeDetailContentMore.isVisible = true
                    }
                    binding.tvHomeDetailContentMore.setOnClickListener {
                        binding.tvHomeDetailContent.maxLines = Int.MAX_VALUE
                        binding.tvHomeDetailContentMore.isVisible = false
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initTopImageViewPager() {
        binding.vp2HomeDetail.adapter = HomeDetailImageAdapter()
        TabLayoutMediator(binding.tlHomeDetailIndicator, binding.vp2HomeDetail) { tab, position ->
            binding.vp2HomeDetail.currentItem = tab.position
        }.attach()
    }

    private fun initPlaceTab() {

        val tabTitle = arrayOf("맛집", "주변 여행지", "숙박")
        viewModel.detailInfo.value?.let { value ->
            binding.vp2HomeDetailMap.adapter = HomeDetailPlaceViewPagerAdapter(value, this, childFragmentManager, lifecycle)
            TabLayoutMediator(binding.tlHomeDetailPlace, binding.vp2HomeDetailMap) { tab, position ->
                tab.text = tabTitle[position]
            }.attach()
            binding.vp2HomeDetailMap.isUserInputEnabled = false
            binding.vp2HomeDetailMap.isSaveEnabled = false
        }

    }

    private fun initTextLink() {
        binding.tvHomeDetailTel.setOnClickListener {
            try {
                Intent(Intent.ACTION_DIAL).also {
                    it.data = Uri.parse("tel:${binding.tvHomeDetailTel.text}")
                    startActivity(it)
                }
            }catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }

        }
        binding.tvHomeDetailHomepage.setOnClickListener {
            val clipboard: ClipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("homepage", binding.tvHomeDetailHomepage.text)
            clipboard.setPrimaryClip(clip)
            toast("클립보드 복사 되었습니다.")
        }

        binding.tvHomeDetailTel.post {
            setUnderLineText(binding.tvHomeDetailTel)
            setUnderLineText(binding.tvHomeDetailHomepage)
        }
    }

}