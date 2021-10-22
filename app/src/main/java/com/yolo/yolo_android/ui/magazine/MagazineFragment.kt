package com.yolo.yolo_android.ui.magazine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yolo.yolo_android.MAGAZINE_URL
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.common.extensions.SwipeRefreshLayoutExt.setRefreshAttrChanged
import com.yolo.yolo_android.common.extensions.ToastExt.toast
import com.yolo.yolo_android.common.extensions.ViewExt.openExternalWebView
import com.yolo.yolo_android.databinding.FragmentMagazineBinding
import com.yolo.yolo_android.ui.dialog.ConfirmCancelDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MagazineFragment : BindingFragment<FragmentMagazineBinding>(R.layout.fragment_magazine) {
    private val magazineViewModel: MagazineViewModel by viewModels()
    private val magazineAdapter = MagazineAdapter()
    private val magazineOffsetPx by lazy {
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.magazine_page_margin)
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.magazine_width)
        val screenMarginStart = resources.getDimensionPixelOffset(R.dimen.default_margin_24)
        val screenWidth = resources.displayMetrics.widthPixels
        screenWidth - pageMarginPx - pagerWidth - screenMarginStart
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = magazineViewModel
        binding.swipeRefreshLayout.setOnRefreshListener {
            magazineViewModel.refresh()
            magazineViewModel.loading.observe(viewLifecycleOwner, {
                binding.swipeRefreshLayout.setRefreshAttrChanged(it)
            })
        }
        binding.vpMagazine.apply {
            adapter = magazineAdapter
            setPageTransformer { page, position ->
                page.translationX = position * -magazineOffsetPx
            }
        }
        binding.tvOpenMagazine.setOnClickListener {
            it.openExternalWebView(MAGAZINE_URL)
        }
        binding.ivSubscribe.setOnClickListener {
            magazineViewModel.subscribing.value?.let { subscribing ->
                if (subscribing) {
                    showCancelDialog()
                } else {
                    magazineViewModel.subscribe()
                }
            }
        }
        magazineViewModel.init()
        observeViewModel()
    }

    private fun observeViewModel() {
        magazineViewModel.toastMessage.observe(viewLifecycleOwner, { toast(it) })
        magazineViewModel.magazineList.observe(viewLifecycleOwner, {
            magazineAdapter.setData(it)
            binding.vpMagazine.offscreenPageLimit = if (it.isEmpty()) 1 else it.size
        })
    }

    private fun showCancelDialog() {
        val dialog = ConfirmCancelDialog
            .newInstance(
                msg = getString(R.string.magazine_message_subscribe_cancel),
                confirm = {
                    magazineViewModel.cancelSubscription()
                }
            )
        dialog.show(childFragmentManager, ConfirmCancelDialog::class.java.simpleName)
    }

    companion object {
        fun newInstance(): MagazineFragment {
            return MagazineFragment()
        }
    }
}